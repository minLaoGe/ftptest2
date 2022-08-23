package com.example.ftptest2.utils;

import com.example.ftptest2.config.netty.ChatHandler;
import com.example.ftptest2.enitity.TpcCredentialsDTO;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;

import java.io.*;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 *
 * @author shussain
 * @version 0.1
 */
@Slf4j
public class FtpTestClient {
     
    public final static int DEFAULT_FTP_PORT = 21;
     
    private FTPClient ftpc;
         
    public static void main(String[] args) {
        String host_names = "192.20.96.50";
        String user_names = "app";
        String passwords =  "hzsmktest";
        String src = "/home/app/logs/mobilemsg/20220823/mobilemsg/msg.log";
        String ftp_src_file = "msg.log";
        int port=10022;
         
       /* String[] host_names = {"192.20.96.50"};
        String[] user_names = {"app"};
        String[] passwords =  {"hzsmktest"};
        String ftp_src_folder = "/home/app/logs/mobilemsg/20220823/mobilemsg";
        String ftp_src_file = "msg.log";
        int port=10022;

        for (int i = 0; i < host_names.length; i++) {
            FtpTestClient.getFile(host_names[i], user_names[i],
                passwords[i], ftp_src_folder, ftp_src_file,port);
        }*/
        TpcCredentialsDTO tpcCredentialsDTO = new TpcCredentialsDTO().setHost(host_names).setPort(port).setUsername(user_names).setPassword(passwords).setFileServersPath(src)
                .setLocalPath(ftp_src_file).setFileServersCommand("tail -f "+src);
        downloadFile(tpcCredentialsDTO);
    }


    public FtpTestClient() {

    }
    public static void begin(){
        String host_names = "192.20.96.50";
        String user_names = "app";
        String passwords =  "hzsmktest";
        String src = "/home/app/logs/mobilemsg/20220823/mobilemsg/msg.log";
        String ftp_src_file = "msg.log";
        int port=10022;

       /* String[] host_names = {"192.20.96.50"};
        String[] user_names = {"app"};
        String[] passwords =  {"hzsmktest"};
        String ftp_src_folder = "/home/app/logs/mobilemsg/20220823/mobilemsg";
        String ftp_src_file = "msg.log";
        int port=10022;

        for (int i = 0; i < host_names.length; i++) {
            FtpTestClient.getFile(host_names[i], user_names[i],
                passwords[i], ftp_src_folder, ftp_src_file,port);
        }*/
        TpcCredentialsDTO tpcCredentialsDTO = new TpcCredentialsDTO().setHost(host_names).setPort(port).setUsername(user_names).setPassword(passwords).setFileServersPath(src)
                .setLocalPath(ftp_src_file).setFileServersCommand("tail -f "+src);
        downloadFile(tpcCredentialsDTO);
    }

    public FtpTestClient(String host, String login, String passwd)
    throws Exception
    {
        this(host, String.valueOf(FtpTestClient.DEFAULT_FTP_PORT), login, passwd);
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
    public FtpTestClient(String host, String port, String login, String passwd)
    throws Exception
    {
        try {
            ftpc = new FTPClient();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public static void downloadFile(TpcCredentialsDTO dto) {
        log.trace("Entering downloadFile() method");

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(dto.getUsername(), dto.getHost(),
                    dto.getPort());
            session.setPassword(dto.getPassword());

            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            log.info("Connected to " + dto.getHost() + ".");

            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

             getFilesToDownload(dto,channelSftp,session);

        } catch (Exception ex) {
            log.error("error:",ex);
        }finally {
            if (channelSftp.isConnected()) {
                try {
                    session.disconnect();
                    channel.disconnect();
                    channelSftp.quit();
                } catch (Exception ioe) {
                    log.error("errror",ioe);
                }
            }
        }
        log.trace("Exiting downloadFile() method");
    }

    private static void getFilesToDownload(TpcCredentialsDTO dto, ChannelSftp sftp,Session session) throws SftpException {
//        sftp.cd(dto.getFileServersPath());
        // If you need to display the progress of the upload, read how to do it in the end of the article
        try {
            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(dto.getFileServersCommand());
            ((ChannelExec)channel).setErrStream(System.err);
            InputStream in=channel.getInputStream();
            channel.connect();
            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    String str = new String(tmp, 0, i);
                    log.info(str);
                    for (io.netty.channel.Channel client : ChatHandler.clients) {
                        if (client.isWritable()){
                            client.writeAndFlush(str);
                        }
                    }
                }
                if(channel.isClosed()){
                    log.error("exit-status: "+channel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
            channel.setInputStream(null);
        } catch (Exception e) {
            log.error("服务器断开链接",e);
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
     
     
}