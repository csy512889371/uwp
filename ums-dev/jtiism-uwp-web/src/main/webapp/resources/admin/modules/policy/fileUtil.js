

function fileDialogComplete(numFilesSelected, numFilesQueued) {
    try {
        if (numFilesQueued > 0) {
            document.getElementById('btnCancel').disabled = "";
            //this.startUpload();
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadProgress(file, bytesLoaded) {

    try {
        var percent = Math.ceil((bytesLoaded / file.size) * 100);
        var progress = new FileProgress(file, this.customSettings.upload_target);
        progress.setProgress(percent);
        if (percent === 100) {
            progress.setStatus("");//正在创建缩略图...
            progress.toggleCancel(false, this);
        } else {
            progress.setStatus("正在上传...");
            progress.toggleCancel(true, this);
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function uploadSuccess(file, serverData) {
    try {
        var progress = new FileProgress(file, this.customSettings.upload_target);
        // addFileInfo(file.id,"文件上传完成");
    } catch (ex) {
        this.debug(ex);
    }
}
/*

function deleteFile(fileId,obj) {
    var _this =obj;
    //用表格显示
    var divfileId = "swfu_ldr" + fileId;
    // alert(divfileId);
    var row = document.getElementById(divfileId);
    try {
        var status = $(row).find(".fileLink").attr("status");
        var fileName = $(row).find(".fileLink").attr("href");
        var fileName = fileName.substring(fileName.lastIndexOf("/reasource")+10);
    }
    catch (e){
        var divWrap = $(_this).closest("._gut clearfix");
        var status = $(divWrap).find(".fileLink").attr("status");
        var fileName = $(divWrap).find(".fileLink").attr("href");
        var fileName = fileName.substring(fileName.lastIndexOf("/reasource")+10);
        $("#deleteFileStr").val($("#deleteFileStr").val() + fileName + ",");
        divWrap.remove();
        return;
    }
    if (status == 1) {
        $("#deleteFileStr").val($("#deleteFileStr").val() + fileName + ",");
    }else{
        uploader.cancelFile( fileId );
    }
    row.remove();

    // infoSpan.deleteRow(row.rowIndex);
}*/








function deleteFile(fileId,obj) {
    var _this =obj;
    //用表格显示
    var divfileId = "swfu_ldr" + fileId;
    // alert(divfileId);
    var row = document.getElementById(divfileId);
    try {
        var status = $(row).find(".fileLink").attr("status");
        var fileName = $(row).find(".fileLink").attr("href");
        var fileName = fileName.substring(fileName.lastIndexOf("/reasource")+10);
    }
    catch (e){
        var divWrap = $(_this).closest("._gut.clearfix");
        var status = $(divWrap).find(".fileLink").attr("status");
        var fileName = $(divWrap).find(".fileLink").attr("href");
        var fileName = fileName.substring(fileName.lastIndexOf("/reasource")+10);
        $("#deleteFileStr").val($("#deleteFileStr").val() + fileName + ",");
        divWrap.remove();
        return;
    }
    if (status == 1) {
        $("#deleteFileStr").val($("#deleteFileStr").val() + fileName + ",");
    }else{
        uploader.cancelFile( fileId );
    }
    row.remove();

    // infoSpan.deleteRow(row.rowIndex);
}

function setUploadFileStr() {
    var uploadFileStr = '';
    var elements = $('#infoSpan ._tit a');
    var size = elements.size();
    if(size>0){
        elements.each(
            function (index,item) {
                var status  =$(item).attr("status");
                var fileName = $(item).html();
                if(status&&status==0){
                    uploadFileStr+=  fileName;
                    if(index+1<size){
                        uploadFileStr+= ","
                    }
                }
            }
        );
//            alert(uploadFileStr);
        $("#uploadFileStr").val(uploadFileStr);
    }
}



/* ******************************************
 *	FileProgress Object
 *	Control object for displaying file info
 * ****************************************** */

function FileProgress(file, targetID) {
    this.fileProgressElement = document.getElementById("swfu_ldr-" + file.id + "-prg");
    //
    /*
        this.fileProgressID = "divFileProgress";//进度条包装的DIV

        this.fileProgressWrapper = document.getElementById(this.fileProgressID);
        if (!this.fileProgressWrapper) {
            this.fileProgressWrapper = document.createElement("div");
            this.fileProgressWrapper.className = "progressWrapper";
            this.fileProgressWrapper.id = this.fileProgressID;

            this.fileProgressElement = document.createElement("div");
            this.fileProgressElement.className = "progressContainer";

            var progressCancel = document.createElement("a");
            progressCancel.className = "progressCancel";
            progressCancel.href = "#";
            progressCancel.style.visibility = "hidden";
            progressCancel.appendChild(document.createTextNode(" "));

            var progressText = document.createElement("div");
            progressText.className = "progressName";
            progressText.appendChild(document.createTextNode("上传文件: "+file.name));

            var progressBar = document.createElement("div");
            progressBar.className = "progressBarInProgress";

            var progressStatus = document.createElement("div");
            progressStatus.className = "progressBarStatus";
            progressStatus.innerHTML = "&nbsp;";

            this.fileProgressElement.appendChild(progressCancel);
            this.fileProgressElement.appendChild(progressText);
            this.fileProgressElement.appendChild(progressStatus);
            this.fileProgressElement.appendChild(progressBar);

            this.fileProgressWrapper.appendChild(this.fileProgressElement);
            document.getElementById(targetID).style.height = "75px";
            document.getElementById(targetID).appendChild(this.fileProgressWrapper);
            fadeIn(this.fileProgressWrapper, 0);

        } else {
            this.fileProgressElement = this.fileProgressWrapper.firstChild;
            this.fileProgressElement.childNodes[1].firstChild.nodeValue = "上传文件: "+file.name;
        }

        this.height = this.fileProgressWrapper.offsetHeight;*/

}

FileProgress.prototype.setProgress = function (percentage) {
    // this.fileProgressElement.className = "progressContainer green";
    // this.fileProgressElement.className = "progressBarInProgress";
    this.fileProgressElement.style.width = percentage + "%";

    if (percentage == 100) {
        this.fileProgressElement.style.width = "0px";
        $(this.fileProgressElement).parent().width("0px");
    }

};
FileProgress.prototype.setComplete = function () {
    // this.fileProgressElement.className = "progressContainer blue";
    // this.fileProgressElement.className = "progressBarComplete";
    // this.fileProgressElement.style.width = "";

};
FileProgress.prototype.setError = function () {
    this.fileProgressElement.className = "progressContainer red";
    this.fileProgressElement.childNodes[3].className = "progressBarError";
    this.fileProgressElement.childNodes[3].style.width = "";

};
FileProgress.prototype.setCancelled = function () {
    this.fileProgressElement.className = "progressContainer";
    this.fileProgressElement.childNodes[3].className = "progressBarError";
    this.fileProgressElement.childNodes[3].style.width = "";

};
FileProgress.prototype.setStatus = function (status) {
    this.fileProgressElement.childNodes[2].innerHTML = status;
};

FileProgress.prototype.toggleCancel = function (show, swfuploadInstance) {
    this.fileProgressElement.childNodes[0].style.visibility = show ? "visible" : "hidden";
    if (swfuploadInstance) {
        var fileId = this.fileProgressID;
        this.fileProgressElement.childNodes[0].onclick = function () {
            swfuploadInstance.cancelUpload(fileId);
            return false;
        };
    }
};



function initWebUploader(config) {
    uploader = WebUploader.create(config);

    uploader.onFileQueued = function( file ) {
        var fileUrl = "/resource" + tempFileUrl + file.name;
        // alert(fileUrl);
        addReadyFileInfo(file.id, fileUrl, "成功加载到上传队列e+ ", 0);
        var divfileId = "swfu_ldr" + file.id;
    };

    return uploader;

}


//status 0 未上傳 1 已上傳
function addReadyFileInfo(fileId, fileUrl, message, status) {
    var fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    var ext = fileName.substring(fileName.lastIndexOf(".") + 1);
    var fileIconClass = (ext == '' ? '' : "i-file-" + ext);
    var iconHtml = '<div class="i-c-g i-file i-file-dll '+fileIconClass+'" style="margin:5px 5px 0 0;float: left"></div>';
    //用表格显示
    var divfileId = "swfu_ldr" + fileId;
    var infoSpan = document.getElementById("infoSpan");
    var btmHtml = "<div id=\"swfu_ldr-" + fileId + "-oper\" class=\"_op\"><div class=\"_btnwrp\"><div class=\"_btn\"><a  href='javascript:void(0)'  onclick= 'deleteFile(\"" + fileId + "\",this)'>删除</a></div>";
    var titleHtml = '<div id="swfu_ldr-' + fileId + '-lk" class="_tit fix" style="min-width:135px;max-width:200px" title="' + fileName + '"><a class="fileLink"  status=' + status + '  href="' + fileUrl + '" onclick=""  download ="'+fileName+'"    >' + fileName + '</a></div>';
    var barDivHtml = "<div class=\"_bar\"><div id=\"swfu_ldr-" + fileId + "-prg\" class=\"_prg\" style=\"width: 100%;\"></div></div>";
    var divHtmkl = "<div class=\"_gut clearfix\"><div id=" + divfileId + " class=\"pl-fileupload-loader pl-fileupload-loader-small\">" + barDivHtml +iconHtml+ titleHtml + btmHtml + "</div></div>";
    infoSpan.innerHTML = infoSpan.innerHTML + divHtmkl;
}



//status 0 未上傳 1 已上傳
function addMemberAttrFile(fileId, fileUrl, status) {
    var fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    var ext = fileName.substring(fileName.lastIndexOf(".") + 1);
    var fileIconClass = (ext == '' ? '' : "i-file-" + ext);
    //用表格显示
    var divfileId = "swfu_ldr" + fileId;
    var infoSpan = document.getElementById("infoSpan");
    var titleHtml = '<div id="swfu_ldr-' + fileId + '-lk" class="_tit fix" style="min-width:135px;max-width:200px" title="' + fileName + '"><a class="fileLink"   href="' + fileUrl + '" onclick="" download ="'+fileName+'">' + fileName + '</a></div>';
    var divHtmkl = "<div class=\"_gut clearfix\"><div id=" + divfileId + " class=\"pl-fileupload-loader pl-fileupload-loader-small\">" + titleHtml + "</div></div>";
    infoSpan.innerHTML = infoSpan.innerHTML + '<div><div class="i-c-g i-file ' + fileIconClass + ' left" style="margin:5px 5px 0 0;"></div><div><a href="' + fileUrl + '" >' + fileName + '</a></div></div>';
}


//根据文件字符串初始化附件列表
function initFileAttrs(fileStr) {
    if (fileStr) {
        var arr = fileStr.split(",");
        for (var i = 0; i < arr.length; i++) {
            var fileName = arr[i];
            if (fileName) {
                addReadyFileInfo(fileName, "/resource/" + fileName, "", 1);
            }
        }
    }
}

//根据文件字符串初始化附件列表
function initMemberFileAttrs(fileStr) {
    var infoSpan = document.getElementById("infoSpan");
    if (fileStr) {
        var arr = fileStr.split(",");
        for (var i = 0; i < arr.length; i++) {
            var fileName = arr[i];
            if (fileName) {
                addMemberAttrFile(fileName, "/resource/" + fileName, "", 1);
            }
        }
    }
}


