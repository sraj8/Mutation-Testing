package eclipse.ast.parser;

import eclipse.ast.parser.Dao.ExpressionParametersDaoImpl;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ASTParserClass {

    static Document document;

    static final String keywords[] = { "abstract", "assert", "boolean",
            "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "extends", "false",
            "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native",
            "new", "null", "package", ";", "class", "INSTANCE", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "true",
            "try", "void", "volatile", "while" , "arrayBaseOffset", "UnknownJvmStringHash",
            "ModernHotSpotStringHash", "HotSpotPrior7u6StringHash", "unsignedInt", "Primitives", "unsignedByte"};

    static final String typeKeys[] = {"assert", "break", "case", "continue",
            "do", "else", "for", "goto", "if", "while"};

    static File file;
    static HashSet<String> varList ,methodList;
    static List<String> keyList = new ArrayList<>(Arrays.asList(keywords));
    static ExpressionParametersDaoImpl parametersDao = new ExpressionParametersDaoImpl();


    //use ASTParse to parse string
    public static void parse(String str) {
        varList = new HashSet<>();
        methodList = new HashSet<>();
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);
        parser.setSource(str.toCharArray());
        parser.setUnitName(file.getName());

        String[] sources = {""};
        String[] classpath = {""};
        parser.setEnvironment(classpath,sources,new String[] { "UTF-8" },true);

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        cu.recordModifications();
        ASTRewrite rewrite = ASTRewrite.create(cu.getAST());

        cu.accept(new ASTVisitor() {

            public boolean visit(VariableDeclarationFragment node) {
                varList.add(node.getName() + "");
                return false;
            }

            @Override
            public boolean visit(FieldDeclaration node) {

                for(Iterator iterator = node.fragments().iterator(); iterator.hasNext();){

                    VariableDeclarationFragment fragment = (VariableDeclarationFragment) iterator.next();
                    IVariableBinding binding = fragment.resolveBinding();

                    parametersDao.getBindingMap().put(binding.getName(), binding.toString());
                }

                return false;
            }

            @Override
            public boolean visit(MethodDeclaration node) {
                methodList.add(node.getName() + "");
                methodList.add("reverseBytes");
                return false;
            }
            @Override
            public boolean visit(MethodInvocation node) {

                methodList.add(node.getName().toString());
                return false;
            }

            public  boolean visit(ExpressionStatement node){
                String expression = node.getExpression() + "";
                if (! parametersDao.getExpressions().contains(expression)) {
                    parametersDao.getExpressions().add(expression);
                    getParameters(expression, cu.getLineNumber(node.getStartPosition()), "EXPRESSION");
                }
                return false;
            }


        });

        cu.accept(new ASTVisitor() {
            public  boolean visit(Assignment node){
                StringBuilder AssignStrBuild = new StringBuilder();
                AssignStrBuild.append(node.getLeftHandSide()).append(" ").append(node.getOperator()).
                        append(" ").append(node.getRightHandSide()).append(";");

                String assignmentString = AssignStrBuild.toString();
                String assignstr = assignmentString.replace(" ","");
                if (! parametersDao.getExpressions().contains(assignmentString)) {
                    parametersDao.getExpressions().add(assignmentString);

                    ExpressionParametersDaoImpl expressPara = new ExpressionParametersDaoImpl();

                    StringTokenizer token = new StringTokenizer(assignstr, "+-*/()[]=%>!&'^!,.;", true);
                    while (token.hasMoreTokens()) {
                        String nexttoken = token.nextToken();

                        if ("+-*/()[]=%>!&^!.".contains(nexttoken)) {
                            expressPara.getOperatorList().add(nexttoken);
                        } else if (methodList.contains(nexttoken.toString().trim())) {
                            expressPara.getMethodCall().add(nexttoken);
                        } else {
                            if (!keyList.contains(nexttoken) && !org.apache.commons.lang.StringUtils.isNumericSpace
                                    (nexttoken.toString()))
                                expressPara.getOperandList().add(nexttoken);

                        }
                    }
                    try {
                        try {
                            CreateInstrumStmt.createInstrum(cu.getLineNumber(node.getStartPosition()),
                                    assignmentString.contains("=") ? "ASSIGNMENT" : "STATEMENT",
                                    expressPara.getOperandList(), assignmentString, Paths.get(file.getAbsolutePath()), parametersDao.getBindingMap());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (BadPositionCategoryException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            public boolean visit(SimpleName node) {
                return true;
            }
        });
    }


    //read file content into a string
    public static String readFileToString(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[10];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();
        document = new Document(fileData.toString());

        return  fileData.toString();
    }

    //loop directory to get file list
    public static void ParseFilesInDir() throws IOException{
        File directories = new File(".");
        String dir = directories.getCanonicalPath() +
                File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator+"net"+
                File.separator+"openhft"+File.separator+"hashing"+File.separator;

        String fileLoc= dir + "old_java" + File.separator;
        String delPath = "\\java\\net\\openhft\\hashing";
        fileLoc = fileLoc.replace(delPath,"");
        FileUtils.deleteDirectory(new File(fileLoc));

        File src = new File(dir);
        File[] allFiles = src.listFiles ( );
        String filePath = null;

        for (File allfiles : allFiles ) {
            filePath = allfiles.getAbsolutePath();
            CreateBackup.backupAndCreateFile(allfiles.getPath());
            if(allfiles.isFile()){
                file = allfiles;
                parse(readFileToString(filePath));
            }
        }
    }

    public static void getParameters (String expression, int lineno, String type){
        try {
            try {
                ExpressionParametersDaoImpl parametersDao = new ExpressionParametersDaoImpl();
                tokenizeExpression(expression, parametersDao);
                parametersDao.getNewExpression().append(";");
                String expStmt = parametersDao.getNewExpression().toString();
                expStmt = expStmt.replace(" (", "(");
                expStmt= expStmt.replace(" ) ", ")");
                CreateInstrumStmt.createInstrum( lineno , type,parametersDao.getOperandList() , expStmt,
                        Paths.get(file.getAbsolutePath()), parametersDao.getBindingMap());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (BadPositionCategoryException e) {
            e.printStackTrace();
        }
    }

    public  static  void tokenizeExpression(String expression, ExpressionParametersDaoImpl expressParams)
    {
        StringTokenizer token = new StringTokenizer(expression,"+-*/()[]=%>!&,;",true);
        while(token.hasMoreTokens()){
            String nexttoken = token.nextToken();
            expressParams.getNewExpression().append(nexttoken).append(" ");
            if("+-*/()[]=%>!&,".contains(nexttoken))
            {
                expressParams.getOperatorList().add(nexttoken);
            }
            else if(methodList.contains(nexttoken.toString().trim()))
            {
                expressParams.getMethodCall().add(nexttoken);
            }
            else{
                if(! keyList.contains(nexttoken) &&
                        ! org.apache.commons.lang.StringUtils.isNumericSpace(nexttoken.toString()))
                    expressParams.getOperandList().add(nexttoken);

            }
        }
    }

    public static void main(String[] args) throws IOException {
        ParseFilesInDir();
    }

}