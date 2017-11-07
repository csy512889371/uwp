package rongji.cmis.service.cadreUnit.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import rongji.cmis.dao.cadreUnit.InfoCadreBasicAttributeDao;
import rongji.cmis.dao.system.CfgUmsCodeAttributeDao;
import rongji.cmis.dao.system.DynamicInfoSetDao;
import rongji.cmis.model.sys.SysColumnShow;
import rongji.cmis.model.sys.SysColumnShow.PropertyType;
import rongji.cmis.model.ums.CfgUmsCodeAttribute;
import rongji.cmis.model.ums.RoleInfosetPri;
import rongji.cmis.service.cadreUnit.InfoCadreBasicAttributeService;
import rongji.cmis.service.common.FileService;
import rongji.cmis.service.system.DynamicInfoSetService;
import rongji.cmis.service.system.RoleInfosetPriService;
import rongji.framework.base.model.FileInfo;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Filter.Logic;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.base.pojo.Setting;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.*;
import rongji.framework.util.image.ImageUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

import static rongji.framework.util.DateUtil.logger;

@Service("infoCadreBasicAttributeServiceImpl")
public class InfoCadreBasicAttributeServiceImpl extends BaseServiceImpl<SysColumnShow> implements InfoCadreBasicAttributeService {

    @Resource(name = "infoCadreBasicAttributeDaoImpl")
    InfoCadreBasicAttributeDao infoCadreBasicAttributeDao;

    @Resource(name = "dynamicInfoSetServiceImpl")
    DynamicInfoSetService dynamicInfoSetService;

    @Resource(name = "cfgUmsCodeAttributeDaoImpl")
    private CfgUmsCodeAttributeDao cfgUmsCodeAttributeDao;

    @Resource(name = "roleInfosetPriServiceImpl")
    RoleInfosetPriService roleInfosetPriService;

    @Resource(name = "fileServiceImpl")
    private FileService fileService;

    @Resource(name = "dynamicInfoSetDaoImpl")
    DynamicInfoSetDao dynamicInfoSetDao;

    @Resource(name = "freeMarkerConfigurer")
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Resource(name = "dynaHtmlTemplateUtil")
    private DynaHtmlTemplateUtil dynaHtmlTemplateUtil;

    @Override
    public void saveExtraAttr(SysColumnShow attr) {
        attr.setPropertyType(PropertyType.extra);
        infoCadreBasicAttributeDao.save(attr);
        // 添加到数据库中
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.addFilter(Filter.eq("code_id", "3"));
        paramRequest.addFilter(Filter.eq("attrcode", attr.getInfoSet()));
        // List<CfgUmsCodeAttribute> attrs =
        // cfgUmsCodeAttributeDao.findAllByParamRequest(paramRequest);
        // if (attrs.size() > 0 && !attrs.get(0).getIsBasic()) {
        // 添加列到数据库
        dynamicInfoSetService.addColumn(attr);
        // }
    }

    @Override
    public void updateAPartOfExtra(SysColumnShow attr) {
        SysColumnShow attrOld = infoCadreBasicAttributeDao.find(attr.getId());
        attrOld.setInfoSet(attr.getInfoSet());
        attrOld.setName(attr.getName());
        attrOld.setPropertyName(attr.getPropertyName());
        attrOld.setPropertyCode(attr.getPropertyCode());
        attrOld.setSeq(attr.getSeq());
        attrOld.setType(attr.getType());
        attrOld.setLengthLimit(attr.getLengthLimit());
        attrOld.setValidateRule(attr.getValidateRule());
        attrOld.setComment(attr.getComment());

        attrOld = (SysColumnShow) infoCadreBasicAttributeDao.merge(attrOld);

    }

    @Override
    public List<SysColumnShow> findDynEnabledAttrListByInfoSet(String infoSet) {
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.setOrderDirection(Direction.asc);
        paramRequest.setOrderProperty("seq");
        paramRequest.addFilter(Filter.eq(Logic.and, "infoSet", infoSet));
        paramRequest.addFilter(Filter.eq(Logic.and, "isEnabled", true));
        paramRequest.addFilter(Filter.eq(Logic.and, "propertyType", PropertyType.dynamic));
        return infoCadreBasicAttributeDao.findAllByParamRequest(paramRequest);
    }

    @Override
    public List<SysColumnShow> findExtraEnabledAttrListByInfoSet(String infoSet) {
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.setOrderDirection(Direction.asc);
        paramRequest.setOrderProperty("seq");
        paramRequest.addFilter(Filter.eq(Logic.and, "infoSet", infoSet));
        paramRequest.addFilter(Filter.eq(Logic.and, "isEnabled", true));
        paramRequest.addFilter(Filter.eq(Logic.and, "propertyType", PropertyType.extra));
        List<SysColumnShow> showColumnList = infoCadreBasicAttributeDao.findAllByParamRequest(paramRequest);
        for (SysColumnShow column : showColumnList) {
            //单图片
            String htmlstr = "";
            if ("singeImage".equals(column.getHtmlType())) {

                //html..
                try {
                    Setting setting = SettingUtils.get();
                    StringWriter writer = new StringWriter();
                    Map parmas = new HashMap();
                    parmas.put("propertyName", column.getPropertyName());
                    parmas.put("colId", -666);
                    parmas.put("infoSet", infoSet);
                    List<CfgUmsCodeAttribute> cfgUmsCodeAttributeList = cfgUmsCodeAttributeDao.getTableChineseName(infoSet);
                    if (cfgUmsCodeAttributeList.size() > 0) {
                        //Object[] cfgUmsCodeAttribute = (Object[]) cfgUmsCodeAttributeList.get(0);
                        parmas.put("infoName", cfgUmsCodeAttributeList.get(0).getAttrCode());//cfgUmsCodeAttribute[3]
                    }
                    parmas.put("picStr", setting.getSiteUrl() + "/assets/pic/public-default.png");
                    parmas.put("originPicStr", setting.getSiteUrl() + "/assets/pic/public-default.png");

                    htmlstr = dynaHtmlTemplateUtil.findDynaHtmlByNamed("dynaInfoset." + column.getHtmlType(), parmas);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }


            } else if ("multiFile".equals(column.getHtmlType())){

                try {
                    Setting setting = SettingUtils.get();
                    StringWriter writer = new StringWriter();
                    Map parmas = new HashMap();
                    parmas.put("propertyName", column.getPropertyName());
                    parmas.put("multiFile", "");
                    htmlstr = dynaHtmlTemplateUtil.findDynaHtmlByNamed("dynaInfoset." + column.getHtmlType(), parmas);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            }
            String encodeHtml = null;
            try {
                encodeHtml = URLEncoder.encode(htmlstr, "UTF-8");
                column.setHtmlStr(encodeHtml.replaceAll("\\+", "%20"));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return showColumnList;
    }

    @Override
    public List<SysColumnShow> findExtraEnabledAttrListByInfoSetWithColumn(String infoSet, Map map, String id) {
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.setOrderDirection(Direction.asc);
        paramRequest.setOrderProperty("seq");
        paramRequest.addFilter(Filter.eq(Logic.and, "infoSet", infoSet));
        paramRequest.addFilter(Filter.eq(Logic.and, "isEnabled", true));
        paramRequest.addFilter(Filter.eq(Logic.and, "propertyType", PropertyType.extra));
        List<SysColumnShow> showColumnList = infoCadreBasicAttributeDao.findAllByParamRequest(paramRequest);
        String picStr = SettingUtils.get().getSiteUrl() + "/assets/pic/public-default.png";
        String originPicStr = SettingUtils.get().getSiteUrl() + "/assets/pic/public-default.png";
        for (SysColumnShow column : showColumnList) {
            //单图片
            String htmlstr = "";
            if ("singeImage".equals(column.getHtmlType())) {
                //html..
                if (map.get(column.getPropertyName()) != null && map.get(column.getPropertyName()) != "") {
                    if (map.get(column.getPropertyName()) != null && map.get(column.getPropertyName()) != "") {
                        picStr = map.get(column.getPropertyName()).toString();
                    }
                    Setting setting = SettingUtils.get();
                    originPicStr = setting.getSiteUrl() + "/resource" + setting.getDynaImgPath() + picStr;
                    picStr = setting.getSiteUrl() + "/resource" + setting.getDynaImgPath() + picStr;
                }

                try {
                    Map parmas = new HashMap();
                    parmas.put("propertyName", column.getPropertyName());
                    parmas.put("colId", id);
                    parmas.put("infoSet", infoSet);
                    parmas.put("picStr", picStr);
                    parmas.put("originPicStr", originPicStr);
                    List<CfgUmsCodeAttribute> cfgUmsCodeAttributeList = cfgUmsCodeAttributeDao.getTableChineseName(infoSet);
                    if (cfgUmsCodeAttributeList.size() > 0) {
                        //Object[] cfgUmsCodeAttribute = (Object[]) cfgUmsCodeAttributeList.get(0);
                        parmas.put("infoName", cfgUmsCodeAttributeList.get(0).getAttrCode());//cfgUmsCodeAttribute[3]
                    }
                    htmlstr = dynaHtmlTemplateUtil.findDynaHtmlByNamed("dynaInfoset." + column.getHtmlType(), parmas);

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            } else if ("multiFile".equals(column.getHtmlType())){
                String multiFile = "";
                if (map.get(column.getPropertyName()) != null && map.get(column.getPropertyName()) != "") {
                    if (map.get(column.getPropertyName()) != null && map.get(column.getPropertyName()) != ""){
                        multiFile = map.get(column.getPropertyName()).toString();
                    }
                }
                try {
                    Map parmas = new HashMap();
                    parmas.put("propertyName", column.getPropertyName());
                    parmas.put("multiFile", multiFile);
                    htmlstr = dynaHtmlTemplateUtil.findDynaHtmlByNamed("dynaInfoset." + column.getHtmlType(), parmas);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            }
            String encodeHtml = null;
            try {
                encodeHtml = URLEncoder.encode(htmlstr, "UTF-8");
                column.setHtmlStr(encodeHtml.replaceAll("\\+", "%20"));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return showColumnList;
    }

    @Override
    public List<SysColumnShow> findSysColumnShowListByInfoSet(String attrCode) {
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.addFilter(Filter.eq(Logic.and, "infoSet", attrCode));
        return infoCadreBasicAttributeDao.findAllByParamRequest(paramRequest);
    }

    @Override
    public List<SysColumnShow> findFiexdAttrListByInfoSet(String infoSet) {
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.setOrderProperty("seq");
        paramRequest.addFilter(Filter.eq(Logic.and, "infoSet", infoSet));
        paramRequest.addFilter(Filter.eq(Logic.and, "propertyType", PropertyType.fixed));
        return infoCadreBasicAttributeDao.findAllByParamRequest(paramRequest);
    }

    @Override
    public List<SysColumnShow> findInfoSetAttrListByInfoSet(String infoSet) {
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.setOrderProperty("seq");
        paramRequest.addFilter(Filter.eq(Logic.and, "infoSet", infoSet));
        return infoCadreBasicAttributeDao.findAllByParamRequest(paramRequest);
    }

    @Override
    public void deleteDynaColumnByInfoSet(String infoSet) {
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.addFilter(Filter.eq(Logic.and, "propertyType", PropertyType.extra));
        paramRequest.addFilter(Filter.eq(Logic.and, "infoSet", infoSet));
        List<SysColumnShow> columns = infoCadreBasicAttributeDao.findAllByParamRequest(paramRequest);
        for (SysColumnShow column : columns) {
            infoCadreBasicAttributeDao.delete(column);
        }
    }

    @Override
    public List<SysColumnShow> findColumnInfo(String infoSet) {
        return infoCadreBasicAttributeDao.findColumnInfo(infoSet);
    }

    @Override
    public List<SysColumnShow> getColumnPrivs(List<SysColumnShow> attrs, String fromType) {
        for (SysColumnShow scs : attrs) {
            String privCode = scs.getInfoSet() + "_";
            if (StringUtils.isNotEmpty(scs.getPropertyName())) {
                privCode += scs.getPropertyName();
            } else {
                privCode += scs.getPropertyCode();
            }
            RoleInfosetPri rip = roleInfosetPriService.getInfoSetPriv(scs.getInfoSet(), privCode,fromType);
            if (null != rip && null != rip.getPriv()) {
                scs.setColumnPriv(rip.getPriv());
            } else {
                scs.setColumnPriv(null);
            }
        }
        return attrs;
    }

    @Override
    public ResultModel uploadInfoImg(String colId, String colName, String infoSet, MultipartFile imgFile) {
        Setting setting = SettingUtils.get();
        double cadreImageWidth = setting.getCadreImageWidth();
        double cadreImageHeight = setting.getCadreImageHeight();
        ResultModel rm = new ResultModel();

        String filePath = "";
        String fileName = imgFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        ext = ext.toLowerCase();
        String imFile = "";
        if (FileUtil.isImgFile(ext)) {
            if (StringUtils.isNotEmpty(colId)) {
                fileName = "" + colId + "." + ext;
                String jpgName = "" + colId + ".jpg";
                String srcPath = DocResourceUtils.getResourceFilePath("resource");
                String PIC_REL_PATH = setting.getDynaImgPath();
                String THUMB_REL_PATH = setting.getDynaThumbPath();
                String PIC_PATH = srcPath + PIC_REL_PATH;
                String THUMB_PATH = srcPath + THUMB_REL_PATH;

                File picDir = new File(PIC_PATH);
                if (!picDir.exists()) {
                    picDir.mkdirs();
                }
                File thumbDir = new File(THUMB_PATH);
                if (!thumbDir.exists()) {
                    thumbDir.mkdirs();
                }

                File oldUploadFile = new File(PIC_PATH, fileName);
                File oldThumbFile = new File(THUMB_PATH, fileName);
                try {
                    if (oldUploadFile.exists()) {
                        oldUploadFile.delete();
                    }
                    if (oldThumbFile.exists()) {
                        oldThumbFile.delete();
                    }
                    File uploadFile = new File(PIC_PATH, fileName);
                    imgFile.transferTo(uploadFile);
                    Map<String, String> columnMap = new LinkedHashMap<String, String>();
                    if (colName != null) {
                        columnMap.put(colName, fileName);
                        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
                        parameters.put("infoSet", infoSet);
                        parameters.put("columnMap", columnMap);
                        parameters.put("keyName", getKeyName(infoSet));
                        parameters.put("keyValue", colId);
                        String insertSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.updateData", parameters);
                        Integer tg = dynamicInfoSetDao.update(insertSql);
                    } else {
                        rm.setType(rongji.framework.base.model.ResultModel.Type.error);
                        rm.setContent("上传失败，请重新上传!001");
                        logger.error("没有设置上传的字段colName");
                        return rm;
                    }
                } catch (IllegalStateException | IOException e) {
                    rm.setType(rongji.framework.base.model.ResultModel.Type.error);
                    rm.setContent("上传失败，请重新上传!");
                     logger.error(e.getMessage(), e);
                    return rm;
                }
                String THUMB_URL = THUMB_REL_PATH.replaceAll("\\\\", "/");
                String PIC_URL = PIC_REL_PATH.replaceAll("\\\\", "/");
                rm.setParams("/resource/" + THUMB_URL + fileName + ",/resource/" + PIC_URL + '/' + fileName + "?dateTemp=" + DateUtil.getDateTimeStr("yyyyMMddHHmmss") + "," + jpgName);
                String oldPath =  PIC_PATH + fileName;
                String newPath =  THUMB_PATH + jpgName;
                try {
                    BufferedImage bufferedImage = ImageIO.read(new File(PIC_PATH + fileName));
                    double imageWidth = bufferedImage.getWidth();
                    double imageHeight = bufferedImage.getHeight();
                    int width;
                    int height;
                    DecimalFormat df = new DecimalFormat("0.0000");
                    if (imageHeight / imageWidth <= cadreImageHeight / cadreImageWidth) {
                        width = (int) cadreImageWidth;
                        height = (int) (Double.parseDouble(df.format(cadreImageWidth / imageWidth)) * imageHeight);
                    } else {
                        width = (int) (Double.parseDouble(df.format(cadreImageHeight / imageHeight)) * imageWidth);
                        height = (int) cadreImageHeight;
                    }
                    File oldPic = new File(newPath);
                    if (oldPic.exists()) {
                        oldPic.delete();
                    }
                    ImageUtils.zoom(new File(oldPath), new File(newPath), width, height);
                } catch (Exception e) {
                    copyFile(oldPath, newPath);
                }
                imFile = newPath;
            } else {
                filePath = fileService.upload(FileInfo.FileType.temp, imgFile, false);
                imFile = DocResourceUtils.getResourceFilePath(filePath);
                rm.setParams(filePath + "," + filePath + "?dateTemp=" + DateUtil.getDateTimeStr("yyyyMMddHHmmss"));
            }
            BufferedImage bufferedImage;
            int width = 1;
            int height = 1;
            try {
                bufferedImage = ImageIO.read(new File(imFile));
                width = bufferedImage.getWidth();
                height = bufferedImage.getHeight();
            } catch (IOException e) {
            }
            rm.setParams(rm.getParams() + "," + width + "," + height);

            rm.setType(rongji.framework.base.model.ResultModel.Type.success);
            rm.setContent("上传成功！");
        } else {
            rm.setType(rongji.framework.base.model.ResultModel.Type.error);
            rm.setContent("格式错误，请重新上传！");
        }
        return rm;
    }

    public static void copyFile(String oldPath, String newPath) {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                is = new FileInputStream(oldPath); // 读入原文件
                os = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = is.read(buffer)) != -1) {
                    os.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
            if (os != null) {
                IOUtils.closeQuietly(os);
            }
        }
    }


    public String getKeyName(String infoSet) {
        return Constant.getKeyName(infoSet);
    }
}
