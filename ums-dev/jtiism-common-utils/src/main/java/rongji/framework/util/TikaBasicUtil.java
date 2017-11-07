package rongji.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TikaBasicUtil {
	public static FileModel fileToFileModel(File f) {
		Parser parser = new AutoDetectParser();
		InputStream is = null;
		try {
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
			is = new FileInputStream(f);
			ContentHandler handler = new BodyContentHandler();
			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);
			parser.parse(is, handler, metadata, context);
			return new FileModel(metadata, handler);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String fileToTxt(File f) throws Exception {
		Parser parser = new AutoDetectParser();
		InputStream is = null;
		try {
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
			is = new FileInputStream(f);
			ContentHandler handler = new BodyContentHandler();
			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);
			parser.parse(is, handler, metadata, context);
			
			return handler.toString();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String parseBodyToHTML(File f) throws Exception {
	    AutoDetectParser parser = new AutoDetectParser();
		InputStream is = null;
		try {
			Metadata metadata = new Metadata();
			is = new FileInputStream(f);
			ContentHandler handler = new ToXMLContentHandler() ;
			parser.parse(is, handler, metadata);
			String body = handler.toString();
			body = body.substring(body.indexOf("<body>")+6,body.indexOf("</body>"));
			body = body.replaceAll("正文", "default");
			body = body.replaceAll("<img[^>]*>", "");
			//body = body.replaceAll("<div[^'/div']", "");
			body = body.replaceAll("SHAPE \\\\\\* MERGEFORMAT", "");
			body = body.replaceAll("\n", "");
			return body;
		}finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static class FileModel {
		private Metadata metadata;
		private ContentHandler contentHandler;

		public FileModel() {
			super();
		}

		public FileModel(Metadata metadata, ContentHandler contentHandler) {
			super();
			this.metadata = metadata;
			this.contentHandler = contentHandler;
		}

		public Metadata getMetadata() {
			return metadata;
		}

		public void setMetadata(Metadata metadata) {
			this.metadata = metadata;
		}

		public ContentHandler getContentHandler() {
			return contentHandler;
		}

		public void setContentHandler(ContentHandler contentHandler) {
			this.contentHandler = contentHandler;
		}

	}
}