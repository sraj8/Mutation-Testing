package eclipse.ast.parser;

import org.eclipse.jface.text.BadPositionCategoryException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

public class CreateInstrumStmt {
    public static void createInstrum(int lineNo, String type, HashSet<String> operands, String expression, Path path,
                                     HashMap<String, String> bindingMap) throws BadPositionCategoryException, IOException {

        StringBuilder sb =new StringBuilder();

        if(! operands.isEmpty()) {
            sb.append("eclipse.ast.parser.InstrumentationTemplate.instrum(").append("\"");
                    sb.append(" line number ").append("\"").append(" + ").append("\"").append(" | ").append("\"").append(" + ").append(lineNo).append(" + ").append("\"").append(" | ").append("\"").append(" + ").append("\"").append(expression).append("\"").append(" + ").append("\"").append(" | ").append("\"").append(" + ").append("\"").append(path.getFileName()).append("\"").append(" + ").append("\"").append(" | ").append("\"").append(" + ").append("\"").append(" ").append(type).append(" ").append("\"");

            for(String op : operands){
                sb.append(" + ").append("\"").append(" | ").append("\"").append(" + ").append(" \"").append(" ").append(bindingMap.get(op.toString()) != null ? bindingMap.get(op.toString()) : op.toString()).append(" = \"").append(" + ").append(op);
            }
            sb.append(");");
            StringBuilder strbuild = new StringBuilder();
            strbuild.append(expression).append("\r\n").append(sb.toString()).append("\r\n");
            String fileContent = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            fileContent = fileContent.replace(expression,strbuild.toString());
            Files.write(path,fileContent.getBytes(StandardCharsets.UTF_8));
        }



    }
}
