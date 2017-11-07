package rongji.framework.base.dao.dynaHtml;

import org.apache.commons.lang3.Validate;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.internal.util.xml.MappingReader;
import org.hibernate.internal.util.xml.OriginImpl;
import org.hibernate.internal.util.xml.XmlDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import rongji.framework.base.exception.ApplicationException;

import java.io.IOException;
import java.util.*;


public class DefaultDynaHtmlStatementBuilder implements DynaHtmlStatementBuilder, ResourceLoaderAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDynaHtmlStatementBuilder.class);
    private Map<String, String> namedHtmlTemplate;
    private String[] fileNames = new String[0];
    private ResourceLoader resourceLoader;
    private EntityResolver entityResolver = new DynaHtmlStatementDTDEntityResolver();
    /**
     * 查询语句名称缓存，不允许重复
     */
    private Set<String> nameCache = new HashSet<String>();

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    @Override
    public Map<String, String> getHtmlTemplates() {
        return namedHtmlTemplate;
    }

    @Override
    public void init() throws IOException {
        namedHtmlTemplate = new HashMap<String, String>();
        boolean flag = this.resourceLoader instanceof ResourcePatternResolver;
        for (String file : fileNames) {
            if (flag) {
                Resource[] resources = ((ResourcePatternResolver) this.resourceLoader).getResources(file);
                buildMap(resources);
            } else {
                Resource resource = resourceLoader.getResource(file);
                buildMap(resource);
            }
        }
        nameCache.clear();
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private void buildMap(Resource[] resources) throws IOException {
        if (resources == null) {
            return;
        }
        for (Resource resource : resources) {
            buildMap(resource);
        }
    }

    private void buildMap(Resource resource) {
        InputSource inputSource = null;
        try {
            inputSource = new InputSource(resource.getInputStream());
            XmlDocument metadataXml = MappingReader.INSTANCE.readMappingDocument(entityResolver, inputSource, new OriginImpl("file", resource.getFilename()));
            if (isDynamicStatementXml(metadataXml)) {
                final Document doc = metadataXml.getDocumentTree();
                final Element dynamicHibernateStatement = doc.getRootElement();
                Iterator rootChildren = dynamicHibernateStatement.elementIterator();
                while (rootChildren.hasNext()) {
                    final Element element = (Element) rootChildren.next();
                    final String elementName = element.getName();
                    if ("html-tmplate".equals(elementName)) {
                        putStatementToCacheMap(resource, element, namedHtmlTemplate);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
            throw new ApplicationException(e);
        } finally {
            if (inputSource != null && inputSource.getByteStream() != null) {
                try {
                    inputSource.getByteStream().close();
                } catch (IOException e) {
                    LOGGER.error(e.toString());
                    throw new ApplicationException(e);
                }
            }
        }

    }

    private void putStatementToCacheMap(Resource resource, final Element element, Map<String, String> statementMap) throws IOException {
        String htmlTemplateName = element.attribute("name").getText();
        Validate.notEmpty(htmlTemplateName);
        if (nameCache.contains(htmlTemplateName)) {
            throw new ApplicationException("重复的html-tmplate语句定义在文件:" + resource.getURI() + "中，必须保证name的唯一.");
        }
        nameCache.add(htmlTemplateName);
        String queryText = element.getText();
        statementMap.put(htmlTemplateName, queryText);
    }

    private static boolean isDynamicStatementXml(XmlDocument xmlDocument) {
        return "dyna-html-hibernate-statement".equals(xmlDocument.getDocumentTree().getRootElement().getName());
    }
}
