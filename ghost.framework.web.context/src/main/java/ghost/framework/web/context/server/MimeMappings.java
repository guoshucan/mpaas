/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ghost.framework.web.context.server;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.IGetDomain;
import ghost.framework.util.Assert;

import java.util.*;


/**
 * Simple server-independent abstraction for mime mappings. Roughly equivalent to the
 * {@literal &lt;mime-mapping&gt;} element traditionally found in web.xml.
 *
 * @author Phillip Webb
 * @since 2.0.0
 */
@Component
public class MimeMappings implements Iterable<MimeMappings.Mapping> {

	/**
	 * Default mime mapping commonly used.
	 */
	public static final Map<String, String> DEFAULT;
	static {
		DEFAULT = new LinkedHashMap<>();
		DEFAULT.put("abs", "audio/x-mpeg");
		DEFAULT.put("ai", "application/postscript");
		DEFAULT.put("aif", "audio/x-aiff");
		DEFAULT.put("aifc", "audio/x-aiff");
		DEFAULT.put("aiff", "audio/x-aiff");
		DEFAULT.put("aim", "application/x-aim");
		DEFAULT.put("art", "image/x-jg");
		DEFAULT.put("asf", "video/x-ms-asf");
		DEFAULT.put("asx", "video/x-ms-asf");
		DEFAULT.put("au", "audio/basic");
		DEFAULT.put("avi", "video/x-msvideo");
		DEFAULT.put("avx", "video/x-rad-screenplay");
		DEFAULT.put("bcpio", "application/x-bcpio");
		DEFAULT.put("bin", "application/octet-stream");
		DEFAULT.put("bmp", "image/bmp");
		DEFAULT.put("body", "text/html");
		DEFAULT.put("cdf", "application/x-cdf");
		DEFAULT.put("cer", "application/pkix-cert");
		DEFAULT.put("class", "application/java");
		DEFAULT.put("cpio", "application/x-cpio");
		DEFAULT.put("csh", "application/x-csh");
		DEFAULT.put("css", "text/css");
		DEFAULT.put("dib", "image/bmp");
		DEFAULT.put("doc", "application/msword");
		DEFAULT.put("dtd", "application/xml-dtd");
		DEFAULT.put("dv", "video/x-dv");
		DEFAULT.put("dvi", "application/x-dvi");
		DEFAULT.put("eot", "application/vnd.ms-fontobject");
		DEFAULT.put("eps", "application/postscript");
		DEFAULT.put("etx", "text/x-setext");
		DEFAULT.put("exe", "application/octet-stream");
		DEFAULT.put("gif", "image/gif");
		DEFAULT.put("gtar", "application/x-gtar");
		DEFAULT.put("gz", "application/x-gzip");
		DEFAULT.put("hdf", "application/x-hdf");
		DEFAULT.put("hqx", "application/mac-binhex40");
		DEFAULT.put("htc", "text/x-component");
		DEFAULT.put("htm", "text/html");
		DEFAULT.put("html", "text/html");
		DEFAULT.put("ief", "image/ief");
		DEFAULT.put("jad", "text/vnd.sun.j2me.app-descriptor");
		DEFAULT.put("jar", "application/java-archive");
		DEFAULT.put("java", "text/x-java-source");
		DEFAULT.put("jnlp", "application/x-java-jnlp-file");
		DEFAULT.put("jpe", "image/jpeg");
		DEFAULT.put("jpeg", "image/jpeg");
		DEFAULT.put("jpg", "image/jpeg");
		DEFAULT.put("js", "application/javascript");
		DEFAULT.put("jsf", "text/plain");
		DEFAULT.put("json", "application/json");
		DEFAULT.put("jspf", "text/plain");
		DEFAULT.put("kar", "audio/midi");
		DEFAULT.put("latex", "application/x-latex");
		DEFAULT.put("m3u", "audio/x-mpegurl");
		DEFAULT.put("mac", "image/x-macpaint");
		DEFAULT.put("man", "text/troff");
		DEFAULT.put("mathml", "application/mathml+xml");
		DEFAULT.put("me", "text/troff");
		DEFAULT.put("mid", "audio/midi");
		DEFAULT.put("midi", "audio/midi");
		DEFAULT.put("mif", "application/x-mif");
		DEFAULT.put("mov", "video/quicktime");
		DEFAULT.put("movie", "video/x-sgi-movie");
		DEFAULT.put("mp1", "audio/mpeg");
		DEFAULT.put("mp2", "audio/mpeg");
		DEFAULT.put("mp3", "audio/mpeg");
		DEFAULT.put("mp4", "video/mp4");
		DEFAULT.put("mpa", "audio/mpeg");
		DEFAULT.put("mpe", "video/mpeg");
		DEFAULT.put("mpeg", "video/mpeg");
		DEFAULT.put("mpega", "audio/x-mpeg");
		DEFAULT.put("mpg", "video/mpeg");
		DEFAULT.put("mpv2", "video/mpeg2");
		DEFAULT.put("ms", "application/x-wais-source");
		DEFAULT.put("nc", "application/x-netcdf");
		DEFAULT.put("oda", "application/oda");
		DEFAULT.put("odb", "application/vnd.oasis.opendocument.database");
		DEFAULT.put("odc", "application/vnd.oasis.opendocument.chart");
		DEFAULT.put("odf", "application/vnd.oasis.opendocument.formula");
		DEFAULT.put("odg", "application/vnd.oasis.opendocument.graphics");
		DEFAULT.put("odi", "application/vnd.oasis.opendocument.image");
		DEFAULT.put("odm", "application/vnd.oasis.opendocument.text-master");
		DEFAULT.put("odp", "application/vnd.oasis.opendocument.presentation");
		DEFAULT.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
		DEFAULT.put("odt", "application/vnd.oasis.opendocument.text");
		DEFAULT.put("otg", "application/vnd.oasis.opendocument.graphics-template");
		DEFAULT.put("oth", "application/vnd.oasis.opendocument.text-web");
		DEFAULT.put("otp", "application/vnd.oasis.opendocument.presentation-template");
		DEFAULT.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template ");
		DEFAULT.put("ott", "application/vnd.oasis.opendocument.text-template");
		DEFAULT.put("ogx", "application/ogg");
		DEFAULT.put("ogv", "video/ogg");
		DEFAULT.put("oga", "audio/ogg");
		DEFAULT.put("ogg", "audio/ogg");
		DEFAULT.put("otf", "application/x-font-opentype");
		DEFAULT.put("spx", "audio/ogg");
		DEFAULT.put("flac", "audio/flac");
		DEFAULT.put("anx", "application/annodex");
		DEFAULT.put("axa", "audio/annodex");
		DEFAULT.put("axv", "video/annodex");
		DEFAULT.put("xspf", "application/xspf+xml");
		DEFAULT.put("pbm", "image/x-portable-bitmap");
		DEFAULT.put("pct", "image/pict");
		DEFAULT.put("pdf", "application/pdf");
		DEFAULT.put("pgm", "image/x-portable-graymap");
		DEFAULT.put("pic", "image/pict");
		DEFAULT.put("pict", "image/pict");
		DEFAULT.put("pls", "audio/x-scpls");
		DEFAULT.put("png", "image/png");
		DEFAULT.put("pnm", "image/x-portable-anymap");
		DEFAULT.put("pnt", "image/x-macpaint");
		DEFAULT.put("ppm", "image/x-portable-pixmap");
		DEFAULT.put("ppt", "application/vnd.ms-powerpoint");
		DEFAULT.put("pps", "application/vnd.ms-powerpoint");
		DEFAULT.put("ps", "application/postscript");
		DEFAULT.put("psd", "image/vnd.adobe.photoshop");
		DEFAULT.put("qt", "video/quicktime");
		DEFAULT.put("qti", "image/x-quicktime");
		DEFAULT.put("qtif", "image/x-quicktime");
		DEFAULT.put("ras", "image/x-cmu-raster");
		DEFAULT.put("rdf", "application/rdf+xml");
		DEFAULT.put("rgb", "image/x-rgb");
		DEFAULT.put("rm", "application/vnd.rn-realmedia");
		DEFAULT.put("roff", "text/troff");
		DEFAULT.put("rtf", "application/rtf");
		DEFAULT.put("rtx", "text/richtext");
		DEFAULT.put("sfnt", "application/font-sfnt");
		DEFAULT.put("sh", "application/x-sh");
		DEFAULT.put("shar", "application/x-shar");
		DEFAULT.put("sit", "application/x-stuffit");
		DEFAULT.put("snd", "audio/basic");
		DEFAULT.put("src", "application/x-wais-source");
		DEFAULT.put("sv4cpio", "application/x-sv4cpio");
		DEFAULT.put("sv4crc", "application/x-sv4crc");
		DEFAULT.put("svg", "image/svg+xml");
		DEFAULT.put("svgz", "image/svg+xml");
		DEFAULT.put("swf", "application/x-shockwave-flash");
		DEFAULT.put("t", "text/troff");
		DEFAULT.put("tar", "application/x-tar");
		DEFAULT.put("tcl", "application/x-tcl");
		DEFAULT.put("tex", "application/x-tex");
		DEFAULT.put("texi", "application/x-texinfo");
		DEFAULT.put("texinfo", "application/x-texinfo");
		DEFAULT.put("tif", "image/tiff");
		DEFAULT.put("tiff", "image/tiff");
		DEFAULT.put("tr", "text/troff");
		DEFAULT.put("tsv", "text/tab-separated-values");
		DEFAULT.put("ttf", "application/x-font-ttf");
		DEFAULT.put("txt", "text/plain");
		DEFAULT.put("ulw", "audio/basic");
		DEFAULT.put("ustar", "application/x-ustar");
		DEFAULT.put("vxml", "application/voicexml+xml");
		DEFAULT.put("xbm", "image/x-xbitmap");
		DEFAULT.put("xht", "application/xhtml+xml");
		DEFAULT.put("xhtml", "application/xhtml+xml");
		DEFAULT.put("xls", "application/vnd.ms-excel");
		DEFAULT.put("xml", "application/xml");
		DEFAULT.put("xpm", "image/x-xpixmap");
		DEFAULT.put("xsl", "application/xml");
		DEFAULT.put("xslt", "application/xslt+xml");
		DEFAULT.put("xul", "application/vnd.mozilla.xul+xml");
		DEFAULT.put("xwd", "image/x-xwindowdump");
		DEFAULT.put("vsd", "application/vnd.visio");
		DEFAULT.put("wav", "audio/x-wav");
		DEFAULT.put("wbmp", "image/vnd.wap.wbmp");
		DEFAULT.put("wml", "text/vnd.wap.wml");
		DEFAULT.put("wmlc", "application/vnd.wap.wmlc");
		DEFAULT.put("wmls", "text/vnd.wap.wmlsc");
		DEFAULT.put("wmlscriptc", "application/vnd.wap.wmlscriptc");
		DEFAULT.put("wmv", "video/x-ms-wmv");
		DEFAULT.put("woff", "application/font-woff");
		DEFAULT.put("woff2", "application/font-woff2");
		DEFAULT.put("wrl", "entity/vrml");
		DEFAULT.put("wspolicy", "application/wspolicy+xml");
		DEFAULT.put("z", "application/x-compress");
		DEFAULT.put("zip", "application/zip");
	}

	private final Map<String, Mapping> map;

	/**
	 * Create a new empty {@link MimeMappings} instance.
	 */
	@Constructor//注释构建类型入口位置
	public MimeMappings() {
		this(DEFAULT);
	}
	/**
	 * Create a new {@link MimeMappings} instance from the specified mappings.
	 *
	 * @param mappings the source mappings
	 */
	public MimeMappings(MimeMappings mappings) {
		this(mappings, true);
	}

	/**
	 * Create a new {@link MimeMappings} from the specified mappings.
	 *
	 * @param mappings the source mappings with extension as the value and mime-depend as the
	 *                 value
	 */
	public MimeMappings(Map<String, String> mappings) {
		Assert.notNull(mappings, "Mappings must not be null");
		this.map = new LinkedHashMap<>();
		mappings.forEach(this::add);
	}

	/**
	 * Internal constructor.
	 *
	 * @param mappings source mappings
	 * @param mutable  if the new object should be mutable.
	 */
	private MimeMappings(MimeMappings mappings, boolean mutable) {
		Assert.notNull(mappings, "Mappings must not be null");
		this.map = (mutable ? new LinkedHashMap<>(mappings.map) : Collections.unmodifiableMap(mappings.map));
	}

	@Override
	public Iterator<Mapping> iterator() {
		return getAll().iterator();
	}

	/**
	 * Returns all defined mappings.
	 *
	 * @return the mappings.
	 */
	public Collection<Mapping> getAll() {
		return this.map.values();
	}

	/**
	 * Add a new mime mapping.
	 *
	 * @param extension the file extension (excluding '.')
	 * @param mimeType  the mime depend to map
	 * @return any previous mapping or {@code null}
	 */
	public String add(String extension, String mimeType) {
		Mapping previous = this.map.put(extension, new Mapping(extension, mimeType));
		return (previous != null) ? previous.getMimeType() : null;
	}

	/**
	 * Get a mime mapping for the given extension.
	 *
	 * @param extension the file extension (excluding '.')
	 * @return a mime mapping or {@code null}
	 */
	public String get(String extension) {
		Mapping mapping = this.map.get(extension);
		return (mapping != null) ? mapping.getMimeType() : null;
	}

	/**
	 * Remove an existing mapping.
	 *
	 * @param extension the file extension (excluding '.')
	 * @return the removed mime mapping or {@code null} if no item was removed
	 */
	public String remove(String extension) {
		Mapping previous = this.map.remove(extension);
		return (previous != null) ? previous.getMimeType() : null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof MimeMappings) {
			MimeMappings other = (MimeMappings) obj;
			return this.map.equals(other.map);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.map.hashCode();
	}

	/**
	 * Create a new unmodifiable view of the specified mapping. Methods that attempt to
	 * modify the returned map will throw {@link UnsupportedOperationException}s.
	 *
	 * @param mappings the mappings
	 * @return an unmodifiable view of the specified mappings.
	 */
	public static MimeMappings unmodifiableMappings(MimeMappings mappings) {
		return new MimeMappings(mappings, false);
	}

	/**
	 * 使用域类型
	 */
	public static class DomainMapping extends Mapping implements IGetDomain {
		private Object domain;

		/**
		 * 获取域对象
		 *
		 * @return
		 */
		@Override
		public Object getDomain() {
			return domain;
		}

		public DomainMapping(Object domain, String extension, String mimeType) {
			super(extension, mimeType);
			Assert.notNull(domain, "domain must not be null");
			this.domain = domain;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			if (!super.equals(o)) return false;
			DomainMapping that = (DomainMapping) o;
			return super.equals(o) && domain.equals(that.domain);
		}

		@Override
		public int hashCode() {
			return Objects.hash(super.hashCode(), domain);
		}

		@Override
		public String toString() {
			return "Mapping [domain=" + domain.toString() + ", extension=" + this.getExtension() + ", mimeType=" + this.getMimeType() + "]";
		}
	}

	/**
	 * A single mime mapping.
	 */
	public static class Mapping {

		private final String extension;

		private final String mimeType;

		public Mapping(String extension, String mimeType) {
			Assert.notNull(extension, "Extension must not be null");
			Assert.notNull(mimeType, "MimeType must not be null");
			this.extension = extension;
			this.mimeType = mimeType;
		}

		public String getExtension() {
			return this.extension;
		}

		public String getMimeType() {
			return this.mimeType;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Mapping) {
				Mapping other = (Mapping) obj;
				return this.extension.equals(other.extension) && this.mimeType.equals(other.mimeType);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return this.extension.hashCode();
		}

		@Override
		public String toString() {
			return "Mapping [extension=" + this.extension + ", mimeType=" + this.mimeType + "]";
		}
	}
}