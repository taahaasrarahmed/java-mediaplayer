package studiplayer.cert;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Utils {
	private static String[] originalOsSettings = {
			System.getProperty("file.separator"),
			System.getProperty("os.name")
	};
	
	public static char emulateWindows() {
		String[] settings = { "\\", "Windows" };
		return emulateOS(settings);
	}
	
	public static char emulateLinux() {
		String[] settings = { "/", "Linux" };
		return emulateOS(settings);
	}
	
	private static char emulateOS(String[] osSettings) {
		System.setProperty("file.separator", osSettings[0]);
		System.setProperty("os.name", osSettings[1]);		
		return osSettings[0].charAt(0);
	}
	
	public static void resetEmulation() {
		System.setProperty("file.separator", originalOsSettings[0]);
		System.setProperty("os.name", originalOsSettings[1]);		
	}
	
	public static boolean isWindows() {
        String osname = System.getProperty("os.name");
		return osname.toLowerCase().indexOf("win") >= 0; 
	}
	
	protected static void checkMethod(Class<?> targetClass, int modifier, String name, Class<?> returnType, Class<?>... params) {
		String visibility = (Modifier.isPublic(modifier) ? "public " : (Modifier.isProtected(modifier) ? "protected " : (Modifier.isPrivate(modifier) ? "private" : "")));   
		String staticMarker = (Modifier.isStatic(modifier) ? "static " : "");   
		String abstractMarker = (Modifier.isAbstract(modifier) ? "abstract " : "");   
		String expectedMethodHeader = visibility + staticMarker + abstractMarker + returnType.getSimpleName() + " " + name +"(" + String.join(",", Arrays.asList(params).stream().map(el -> el.getSimpleName()).toArray(size -> new String[size])) + ")";
		try {			
			Method method = targetClass.getDeclaredMethod(name, params);
			if(Modifier.isPublic(modifier)) {
				assertTrue("Method should be public, e.g., " + expectedMethodHeader, Modifier.isPublic(method.getModifiers()));
			} else if(Modifier.isPrivate(modifier)) {
				assertTrue("Method should be private, e.g., " + expectedMethodHeader, Modifier.isPrivate(method.getModifiers()));
			} else if(Modifier.isProtected(modifier)) {
				assertTrue("Method should be protected, e.g., " + expectedMethodHeader, Modifier.isProtected(method.getModifiers()));
			}
			if(Modifier.isStatic(modifier)) {
				assertTrue("Method should be static, e.g., " + expectedMethodHeader, Modifier.isStatic(method.getModifiers()));
			} else {
				assertTrue("Method should not be static, e.g., " + expectedMethodHeader, !Modifier.isStatic(method.getModifiers()));
				
				if(Modifier.isAbstract(modifier)) {
					assertTrue("Method should be abstract, e.g., " + expectedMethodHeader, Modifier.isAbstract(method.getModifiers()));
				} else {
					assertTrue("Method should not be abstract, e.g., " + expectedMethodHeader, !Modifier.isAbstract(method.getModifiers()));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("Missing Method " + expectedMethodHeader);
		}
	}
	
	protected static void checkAllowedAccessableMethods(Class<?> targetClass, String...names) {
		Set<String> allowedNames = new HashSet<>();
		for(String name : names) {
			allowedNames.add(name);
		}
		for(Method m : targetClass.getDeclaredMethods()) {
			if(!Modifier.isPrivate(m.getModifiers())) {
				if (m.getName().contains("$")) {
					continue; // skip internal methods like "access$0"
				}
				if(!allowedNames.contains(m.getName())) {
					fail("Method " + m.getName() + " should not be public, protected or package internal. Only " + String.join(", ", names) + " are allowed.");
				}
			}
		}
	}
}

