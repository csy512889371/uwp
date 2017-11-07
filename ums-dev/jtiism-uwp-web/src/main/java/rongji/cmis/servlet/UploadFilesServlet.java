package rongji.cmis.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rongji.framework.util.DocResourceUtils;
import rongji.framework.util.FreemarkerUtils;
import rongji.framework.util.SettingUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 获取上传的图片文件
 */
public class UploadFilesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void fileOutputStream(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("name====>" + request.getParameter("name"));
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存缓冲区，超过后写入临时文件
        factory.setSizeThreshold(10240000);
        // 设置临时文件存储位置
//        String base = "d:/uploadFiles";
        try {
            String uploadPath = SettingUtils.get().getTempUploadPath();
            Map<String, Object> model = new HashMap<>();
            model.put("uuid", UUID.randomUUID().toString());
            String path = FreemarkerUtils.process(uploadPath, model);
            String base = DocResourceUtils.getResourceFilePath("resource") + path;
            File file = new File(base);
            if (!file.exists())
                file.mkdirs();
            factory.setRepository(file);
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置单个文件的最大上传值
            upload.setFileSizeMax(10002400000l);
            // 设置整个request的最大值
            upload.setSizeMax(10002400000l);
            upload.setHeaderEncoding("UTF-8");
            List<?> items = upload.parseRequest(request);
            FileItem item = null;
            String fileName = null;
            for (Object item1 : items) {
                item = (FileItem) item1;
                fileName = base + File.separator + item.getName();
                // 保存文件
                if (!item.isFormField() && item.getName().length() > 0) {
                    item.write(new File(fileName));
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
            logger.error("上传失败，磁盘空间不足");
            request.setAttribute("exception", new FileUploadException("磁盘空间不足"));
            response.setStatus(-200);
            request.getRequestDispatcher("/WEB-INF/views/error/errorUpload.jsp").forward(request, response);
        } catch (Exception e) {
            response.setStatus(-200);
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        fileOutputStream(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        fileOutputStream(req, resp);
    }
}
