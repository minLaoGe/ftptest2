function getUrl(toPath) {
    var url = window.location.href;
    var prot = window.location.protocol;
    var origin = window.location.origin;
    var localhost = origin;
    if(url && url !== ""){
        if (toPath!=null){
            localhost = origin + toPath;
        }
    }
    return localhost;
}
