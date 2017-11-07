/**
 * Created by user on 2017/8/18.
 */
var imageUpload = function (infoset,realpath) {
    return
    "<td width='25%' id='entImage_"+infoset+"' rowspan='4' colspan='2'> "+
        "<label onclick=\"entImgUpload("+infoset+")\">"+
        "<img class='entUplaodImg' id='picture_"+infoset+"' src='http://localhost:8080/resource/pic/{2}.jpg?dateTemp=' data-original='http://localhost:8080/resource/entLicense/{2}.jpg?dateTemp=' onerror=\"this.src='http://localhost:8080/assets/pic/public-default.png'\">"+
        "</label>"+
    "</td>";
}