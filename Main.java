import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static final String root = "D:\\Games\\";

    public static boolean recordLogFile(String log, String fileName) {

        String[] arraysLog = log.toString().split("%");
        try (FileWriter writer = new FileWriter(fileName, true)) {
            for (String string : arraysLog) {
                writer.write(string);
                System.out.println(string);
                writer.append("\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static String userName() throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        String osName = System.getProperty("os.name").toLowerCase();
        String className = null;
        String methodName = "getUsername";
        String userName = "unknown";

        if (osName.contains("windows")) {
            className = "com.sun.security.auth.module.NTSystem";
            methodName = "getName";
        } else if (osName.contains("linux")) {
            className = "com.sun.security.auth.module.UnixSystem";
        } else if (osName.contains("solaris") || osName.contains("sunos")) {
            className = "com.sun.security.auth.module.SolarisSystem";
        }

        if (className != null) {
            Class<?> c = Class.forName(className);
            Method method = c.getDeclaredMethod(methodName);
            Object o = c.newInstance();
            userName = method.invoke(o).toString();
        }
        return userName;
    }

    public static String createStructure(String path, Boolean fileTrueDirFalse) {
        String userName = "Имя пользователя не определено";
        String fullPath = root + path;

        try {
            userName = userName();
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (fileTrueDirFalse) {
            File file = new File(fullPath);

            try {
                if (file.createNewFile()) {
                    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy hh:mm:ss")) +
                            "-Создан файл-" + file.getName() + "-в разделе-"
                            + fullPath + "-пользователь-" + userName + "%";
                } else return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy hh:mm:ss")) +
                        "-Ошибка при создании файла, возможно файл создан ранее" + " в разделе "
                        + fullPath + "-пользователь-" + userName + "%";

            } catch (IOException e) {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy hh:mm:ss"))
                        + "-Ошибка при создании файла-" + file.getName() + "-" + e.getMessage() + "-в разделе-" + fullPath +
                        "-пользователь-" + userName + "%";
            }
        } else {
            File dir = new File(fullPath);
            if (dir.mkdir()) {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy hh:mm:ss")) +
                        "-Создан" + "-раздел-" + fullPath + "-имя пользователя-" + userName + "%";
            } else return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy hh:mm:ss"))
                    + "-Ошибка при создании раздела-" + fullPath + "-пользователь-" + userName + "%";
        }
    }

    public static void main(String[] args) {

        StringBuilder log = new StringBuilder();

        List<String> paths = Arrays.asList("src", "res", "savegames", "temp",
                "src\\main", "src\\test",
                "src\\main\\Main.java", "src\\main\\Utils.java",
                "res\\drawables", "res\\vectors", "res\\icons",
                "temp\\temp.txt");

        for (String path : paths
        ) {
            if (!path.contains(".")) {
                log.append(createStructure(path, false));
            } else {
                log.append(createStructure(path, true));
            }
        }
        if (recordLogFile(log.toString(), root + "temp\\temp.txt")) {
            System.out.println("лог файл успешно записан");
        } else {
            System.out.println("не удалось записать лог файл");
        }
    }
}
