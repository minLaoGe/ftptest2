package com.example.ftptest2.utils;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.ftptest2.config.netty.ChatHandler;
import com.example.ftptest2.enitity.*;
import com.example.ftptest2.service.NettySocketServer;
import com.google.gson.Gson;
import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.*;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 * @author shussain
 * @version 0.1
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FtpTestClient {
     
    public final static int DEFAULT_FTP_PORT = 21;

    private final NettySocketServer messageHander;

    private  Gson gson=new Gson();

    FTPConfigAdopt ftpConfigAdopt =null;
   private ConcurrentHashMap<String,FTPConfigAdopt> hostUsernameAndFtpRelation=new ConcurrentHashMap<>();

   @Async
    public  void begin(){
        ftpConfigAdopt= new FTPConfigAdopt(AllServersUtils.getFtpClient(0));
        EeventEntity eventEntity = EeventEntity.getEventEntity(EventEmum.CHAT_EVENT,ftpConfigAdopt);
       String compliteComand = eventEntity.getCompliteComand();
        eventEntity.setExceptionCommand(compliteComand);
       downloadFile(ftpConfigAdopt,eventEntity);
    }
    public void ListenOtherServers(com.example.ftptest2.enitity.FTPClient ftpClient){

    }



    /**
     * Creates a new instance of FtpTestClient
     * 
     * 
     * @param host Server host name
     * @param port Server port number
     * @param login FTP login name
     * @param passwd FTP login password
     * @throws Exception if client create fails
     */


    public boolean initSessionConnection(FTPConfigAdopt ftpConfigAdopts) {
        JSch jsch = new JSch();

        FTPLogin ftpLogin = ftpConfigAdopts.getFtpLogin();
        Session session=null;
        String key = CommonUtils.getHostNameAndUsername(ftpConfigAdopts);
        try {
            session = jsch.getSession(ftpLogin.getUsername(), ftpLogin.getRemotehost(),
                    ftpLogin.getPort());
            session.setPassword(ftpLogin.getPasssword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ftpConfigAdopts.setSession(session);

            if (!hostUsernameAndFtpRelation.containsKey(key)){
                hostUsernameAndFtpRelation.put(key,ftpConfigAdopts);
            }
            log.info("?????????????????????======{}",gson.toJson(ftpConfigAdopts.getFtpLogin().getRemotehost()));
            return true;
        } catch (JSchException e) {
            log.error("error:",e);
            ftpConfigAdopts.setReconnectCount(ftpConfigAdopts.getReconnectCount()+1);
            if (ftpConfigAdopts.getReconnectCount()<3){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                boolean b = this.initSessionConnection(ftpConfigAdopts);
                if (b){
                    log.info("?????????????????????======{},???????????????{}",gson.toJson(ftpConfigAdopts.getFtpLogin()),ftpConfigAdopts.getReconnectCount());
                    return true;
                }
            }
            hostUsernameAndFtpRelation.remove(key);
            if (!Objects.isNull(session)&&session.isConnected()){
                session.disconnect();
            }
            log.info("?????????????????????======{},???????????????{}",gson.toJson(ftpConfigAdopts.getFtpLogin()),ftpConfigAdopts.getReconnectCount());
            return false;
        }
    }
    public  void downloadFile(FTPConfigAdopt ftpConfigAdopts,EeventEntity eeventEntity) {
        log.trace("Entering downloadFile() method");
        try {
            String hostNameAndUsername = CommonUtils.getHostNameAndUsername(ftpConfigAdopts);
            FTPConfigAdopt  ftpConfigAdopts2=hostUsernameAndFtpRelation.get(hostNameAndUsername);
            if (!Objects.isNull(ftpConfigAdopts2)&&ftpConfigAdopts2.getSession().isConnected()){
                //do Nothing
                beginBoradFirstPayMessage(ftpConfigAdopts2,eeventEntity);
            }else {
                initSessionConnection(ftpConfigAdopts);
                beginBoradFirstPayMessage(ftpConfigAdopts,eeventEntity);

            }


        } catch (Exception ex) {
            log.error("error:",ex);
        }
        log.trace("Exiting downloadFile() method");
    }

    public void beginBoradLoginEvent(String keyword) throws SftpException {
            if (Objects.isNull(ftpConfigAdopt)){
                throw new  RuntimeException("?????????????????????");
            }
        FTPConfigAdopt ftpConfigAdopt1 = new FTPConfigAdopt(AllServersUtils.getFtpClient(1));
        EeventEntity eventEntity = EeventEntity.getEventEntity(EventEmum.LOGIN_EVENT,ftpConfigAdopt1);
            if (StringUtils.isNotEmpty(keyword)){
//                String grepList="'?????????[15927115794]?????????'";
                List<String> strings = Arrays.asList("'"+keyword+"'");
                eventEntity.setKeyWords(strings);
            }
          eventEntity.setBoastEventName(eventEntity.getName()+keyword);
        downloadFile(ftpConfigAdopt1,eventEntity);
    }

    private  void beginBoradFirstPayMessage(FTPConfigAdopt ftpConfigAdopts,EeventEntity event) throws SftpException {
//        sftp.cd(dto.getFileServersPath());
        // If you need to display the progress of the upload, read how to do it in the end of the article
        Channel channel=null;
        try {
                while (true){
                    String compliteComand = event.getCompliteComand();
                    Channel channel1 = ftpConfigAdopts.getComandAndChnnelRealtion().get(compliteComand);
                    if (ftpConfigAdopts.getComandAndChnnelRealtion().containsKey(compliteComand)&&!Objects.isNull(channel1)&&channel1.isConnected()){
                        return;
                    }
                    channel=ftpConfigAdopts.getSession().openChannel("exec");
                    ((ChannelExec)channel).setCommand(event.getCompliteComand());
                    log.info("command:{}",event.getCompliteComand());
                    ftpConfigAdopts.getComandAndChnnelRealtion().put(event.getCompliteComand(),channel);
                    ((ChannelExec)channel).setErrStream(System.err);
                    InputStream in=channel.getInputStream();
                    channel.connect();
                    byte[] tmp=new byte[4096];

                    while(true){
                        while(in.available()>0){
                            int i=in.read(tmp, 0, 4096);
                            if(i<0)break;
                            String str = new String(tmp, 0, i);
                            log.warn(str);
                            log.warn("????????????:{}",event.getBoastEventName());

                            messageHander.handleMessage(event.getBoastEventName(),str);

                        }
                        if(channel.isClosed()){
                            log.error("exit-status: "+channel.getExitStatus());
                            ftpConfigAdopts.setReconnectCount(ftpConfigAdopts.getReconnectCount()+1);
                            messageHander.handleMessage(event.getBoastEventName(),"\n\n??????????????????????????????,,,?????????????????????===========================================");
                            break;
                        }
                        try{Thread.sleep(1000);}catch(Exception ee){}
                    }

                    channel.setInputStream(null);
                    if (ftpConfigAdopts.getReconnectCount()>3){
                        throw new RuntimeException("????????????????????????3????????????");
                    }
                    Thread.sleep(3000);
                }
        } catch (Exception e) {
            if (!Objects.isNull(channel)&&!channel.isClosed()){
                channel.disconnect();
            }
            if (hostUsernameAndFtpRelation.containsKey(event.getHostAndUserName())){
                hostUsernameAndFtpRelation.remove(event.getHostAndUserName());
            }
            if (ftpConfigAdopts.getSession().isConnected()){
                ftpConfigAdopts.getSession().disconnect();
            }
            log.error("?????????????????????",e);
            throw new RuntimeException("????????????????????????3????????????");
        }

        // use the get method , if you are using android remember to remove "file://" and use only the relative path
//        sftp.get(dto.getFileServersPath(),dto.getLocalPath());

    }

    /**
     * get a single file from the server. Destination filename is same as that
     * on server.
     *
     * @param server ftp server name
     * @param username ftp server login name
     * @param password ftp server password
     * @param folder source folder on the ftp server
     * @param fname file to be transferred
     * @return true if successful
     */
    public static boolean getFile(String server, String username,
            String password, String folder, String fname,int port)
    {
        boolean status = false;
         
        FTPClient ftp = null;
        try {
          /*  ftp = new FTPClient();
             
            ftp.connect( server,port);
            ftp.login( username, password );*/
            JSch jsch = new JSch();
            Session session = jsch.getSession( username, server, port );
            session.setConfig( "PreferredAuthentications", "password" );
            session.setPassword( password );
//            session.connect( FTP_TIMEOUT );
            Channel channel = session.openChannel( "sftp" );
            ChannelSftp sftp = ( ChannelSftp ) channel;
//            sftp.connect( FTP_TIMEOUT );
             
            ftp.changeWorkingDirectory( folder );
             
            File file = new File(fname);
            FileOutputStream fos = new FileOutputStream( file );
             
            ftp.retrieveFile(fname, fos);
            fos.close();
             
            status = true;
             
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (Exception e) {}
        }
         
        return status;
    }
     
    public static void getAllFiles( String server,
            String username,
            String password,
            String folder,
            String destinationFolder,
            Calendar start,
            Calendar end ) {
        try {
            // Connect and logon to FTP Server
            FTPClient ftp = new FTPClient();
            ftp.connect( server );
            ftp.login( username, password );
            System.out.println("Connected to " +
                    server + ".");
            System.out.print(ftp.getReplyString());
             
            // List the files in the directory
            ftp.changeWorkingDirectory( folder );
            FTPFile[] files = ftp.listFiles();
            System.out.println( "Number of files in dir: " + files.length );
            DateFormat df = DateFormat.getDateInstance( DateFormat.SHORT );
            for( int i=0; i<files.length; i++ ) {
                Date fileDate = files[ i ].getTimestamp().getTime();
                if( fileDate.compareTo( start.getTime() ) >= 0 &&
                        fileDate.compareTo( end.getTime() ) <= 0 ) {
                    // Download a file from the FTP Server
                    System.out.print( df.format( files[ i ].getTimestamp().getTime() ) );
                    System.out.println( "\t" + files[ i ].getName() );
                    File file = new File( destinationFolder +
                            File.separator + files[ i ].getName() );
                    FileOutputStream fos = new FileOutputStream( file );
                    ftp.retrieveFile( files[ i ].getName(), fos );
                    fos.close();
                    file.setLastModified( fileDate.getTime() );
                }
            }
             
            // Logout from the FTP Server and disconnect
            ftp.logout();
            ftp.disconnect();
             
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
     
    /**
     * Gets all files which matches a specified file name template. The template
     * is a regular expression. 
     *
     * @param server ftp server name
     * @param username ftp server login name
     * @param password ftp server password
     * @param folder source folder on the ftp server
     * @param ftempl regular expression for file name
     * @return true if successful
     * @throws Exception if unable to connect and other problems.
     */
    public static boolean getFilesOfType(String server, String username,
            String password, String folder, String ftempl)
            throws Exception 
    {
        FTPClient ftp = null;
        boolean status = false;
         
        try {
            // Connect and logon to FTP Server
            ftp = new FTPClient();
            ftp.connect( server );
            ftp.login( username, password );
             
            // List the files in the directory
  
            ftp.changeWorkingDirectory( folder );
            FTPFile[] files = ftp.listFiles();
         
            // get files which match name template
            // at present just check for prefix
            for (int i = 0; i < files.length; i++) {
                /*
                 if (files[i].matches(ftempl)) {
                     
                }*/
            }
        } catch (Exception e) {
            throw e;
        }
         
        finally {
            // close connection
            if (ftp != null && ftp.isConnected()) {
                ftp.disconnect();
            }
        }
         
        return status;
    }
     
    /**
     * Upload a single file to the server. Destination filename is same as that
     * on server.
     *
     * @param server ftp server name
     * @param username ftp server login name
     * @param password ftp server password
     * @param folder source folder on the ftp server
     * @param fname file to be transferred
     * @return true if successful
     */
    public boolean putFile(String server, String username,
            String password, String folder, String fname)
    throws Exception {
        return false;
    }
     
    public void setMode(int type) {
         
    }
     
    public int getMode() {
        return -1;
    }


    public void beginLoginMessage() {


    }
}