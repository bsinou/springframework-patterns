package net.sinou.poc.markdown;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class DocController {

	private final static String ROOT_PATH = "/docs";

	@RequestMapping(ROOT_PATH + "/**")
	public String retrieveMdFile(HttpServletRequest request, Model model) {
		String currPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

		String suffix = null;
		if (ROOT_PATH.equals(currPath) || (ROOT_PATH+"/").equals(currPath)) {
			// Corner case that breaks local links.
			String fwdUrl ="forward:"+ROOT_PATH + "/ReadMe.md";
			return fwdUrl;
		} else {
			suffix = currPath.substring(ROOT_PATH.length());
		}

		String md = retrieveMdString(suffix);
		
		model.addAttribute("mdFileContent", md);
		System.out.println("Found suffix: " + suffix);
		return "documentation";
	}

	
	
	private String retrieveMdString(String fileName) {
		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = null;
		try {
			String relPath = "docs/" + fileName;
			file = new File(classLoader.getResource(relPath).getFile());
		} catch (Exception e) {
			e.printStackTrace();
			return result.toString();
		}
		
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
}
