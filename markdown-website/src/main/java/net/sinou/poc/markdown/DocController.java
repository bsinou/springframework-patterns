package net.sinou.poc.markdown;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

/** Almost canonical controller to serve md files as HTML */
@Controller
public class DocController {

	private final static String LOCAL_DOC_ROOT_PATH = "/docs";

	@RequestMapping(value = { "/", "/index.html" })
	public String retrieveHomePage(Model model) throws IOException {

		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;
		try {
			file = new File(classLoader.getResource("Home.md").getFile());
		} catch (Exception e) {
			e.printStackTrace();
			return "documentation";
		}

		String md = getFileContent(file);
		model.addAttribute("mdFileContent", md);
		return "documentation";
	}

	@RequestMapping(LOCAL_DOC_ROOT_PATH + "/**")
	public String retrieveMdFile(HttpServletRequest request, HttpServletResponse response, Model model)
			throws IOException {
		String currPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String suffix = null;
		if (LOCAL_DOC_ROOT_PATH.equals(currPath) || (LOCAL_DOC_ROOT_PATH + "/").equals(currPath)) {
			// Corner case that breaks local links.
			response.sendRedirect(LOCAL_DOC_ROOT_PATH + "/ReadMe.md");
			return null;
		} else {
			suffix = currPath.substring(LOCAL_DOC_ROOT_PATH.length());
		}
		String md = retrieveLocalMdFileAsString(suffix);
		model.addAttribute("mdFileContent", md);
		return "documentation";
	}

	private final static String GIT_DOC_ROOT_PATH = "/gitDocs";
	private final static String GIT_BASE_PATH = "https://raw.githubusercontent.com/bsinou/perso-test/master";

	@RequestMapping(GIT_DOC_ROOT_PATH + "/**")
	public String displayExternalMdFile(HttpServletRequest request, HttpServletResponse response, Model model)
			throws IOException {
		String currPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String suffix = null;
		if (GIT_DOC_ROOT_PATH.equals(currPath) || (GIT_DOC_ROOT_PATH + "/").equals(currPath)) {
			response.sendRedirect(GIT_DOC_ROOT_PATH + "/ReadMe.md");
			return null;
		} else {
			suffix = currPath.substring(GIT_DOC_ROOT_PATH.length());
		}
		String md = retrieveOnlineMdFileAsString(suffix);
		model.addAttribute("mdFileContent", md);
		return "documentation";
	}

	private String retrieveLocalMdFileAsString(String fileName) {

		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;
		try {
			// remove leading space
			String relPath = LOCAL_DOC_ROOT_PATH.substring(1) + fileName;
			file = new File(classLoader.getResource(relPath).getFile());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return getFileContent(file);
	}

	private String getFileContent(File file) {
		StringBuilder result = new StringBuilder("");
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	private String retrieveOnlineMdFileAsString(String suffix) {
		StringBuilder result = new StringBuilder("");
		URL url = null;
		try {
			url = new URL(GIT_BASE_PATH + suffix);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "";
		}

		try (Scanner scanner = new Scanner(url.openStream())) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

}
