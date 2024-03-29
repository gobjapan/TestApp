/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author GOB
 */
public class TestApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        lambdaStreamSample();
    }
    
    //<editor-fold defaultstate="" desc="lambda/stream">
    
    private static void lambdaStreamSample(){
        hatenaSample();
        
        colorSample();
        
        MethodReferenceDemo.main();
        
        GetInterface<DoSomethingInterface> getInterface 
            = ()->()->System.out.println("Hello");
        DoSomethingInterface doSomethingInterface = getInterface.get();
        doSomethingInterface.doSomething();
    
        SampleClass.main();
        
        isNumberInterfaceExec("1");
        isNumberInterfaceExec("test");
        
        SampleFunction function = (name) -> System.out.println("Hello, " + name);
        function.say("test");
        
        IntStream.range(0, 10).filter(n -> n % 2 == 0)
                .forEach(System.out::println);
    }
    
    private static void hatenaSample(){
        //http://d.hatena.ne.jp/nowokay/20130504
        hatenaSample1();
        hatenaSample2_File();
        hatenaSample3();
    }
    
    private static void hatenaSample1(){
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        
        System.out.println(methodName + ":start ----------------");
        
        List<String> names = Arrays.asList("hoge hoge", "foo bar", "naoki", "kishida");
        names.stream()
                .map(s -> "[" + s + "]")
                .filter(s -> s.length() > 5)
                .forEach(System.out::println);
        names.stream()
                .filter(s -> s.length() > 5)
                .map(s -> "[" + s + "]")
                .forEach(System.out::println);
        
        List<String> names2 = Arrays.asList("12", "3 5");
        System.out.println(names.stream().allMatch(s -> !s.isEmpty())); //true
        System.out.println(names.stream().anyMatch(s -> s.length() > 7)); //true
        System.out.println(names.stream().noneMatch(s -> s.startsWith("A"))); //true
        
        //System.out.println(names.stream().collect(Collectors.toStringJoiner(":")) );//err
        StringJoiner sj = new StringJoiner(":", "[", "]");
        names.stream().forEach(s -> sj.add(s));
        System.out.println(sj.toString());//1行で書けないか

        //math
        System.out.println(names2.stream().collect(Collectors.summingLong(s -> s.length())));
        System.out.println(names2.stream().mapToInt(s -> s.length()).sum());

        //list
        List<String> converted = names.stream()
                .filter(s -> s.length() > 5)
                .map(s -> "(" + s + ")")
                .collect(Collectors.toList());
        System.out.println(converted);
        names.stream()
                .collect(Collectors.groupingBy(s -> s.length()))
                .forEach((k,v) -> System.out.println(k + ":" + v));
        Map<Integer, List<String>> map = names.stream()
                .collect(Collectors.groupingBy(s -> s.length()));

        System.out.println(methodName + ":end ----------------");
    }
    
    private static void hatenaSample2_File(){
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        //todo:デリゲート的なの試したい:methodName＋start／endの出力でくくる
        
        System.out.println(methodName + ":start ----------------");        

        String path="C:\\Users\\GOB\\Documents\\NetBeansProjects\\TestApp\\text\\lines.txt";
        try(FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr))
        {
            br.lines().limit(3)
                    .forEach(System.out::println);
        }catch(IOException ex){}
        try{
            Files.readAllLines(Paths.get(path), Charset.defaultCharset())
                    .stream()
                    //.filter(n -> n % 2 == 0)//todo://奇数行だけ出力したい
                    .limit(3)
                    .forEach(System.out::println);
        }catch(IOException ex){}
        
        System.out.println(methodName + ":end ----------------");
    }
    
    private static void hatenaSample3(){
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        //todo:デリゲート的なの試したい:methodName＋start／endの出力でくくる
        
        System.out.println(methodName + ":start ----------------");        

        
        
        System.out.println(methodName + ":end ----------------");
    }
    
    public static void colorSample(){
        String[] values = {"あお","あかいろ","あか","あ","あおいろ","きいろ","みどり","おれんじ"};
        Stream<String> stream = Arrays.stream(values);
        String result = stream.filter(value -> value.contains("あ")) // 【1】
                .max((v1, v2)->v1.compareTo(v2)) // 【2】
                .get();
        System.out.println("result=" + result);
    }
    
    public static class MethodReferenceDemo {
        @FunctionalInterface
        interface Funcion<T> {
            void apply(T value);
        }

        static void print(String text) {
            System.out.println(text);
        }

        public static void main(String... args) {
            //Funcion<String> func = text -> MethodReferenceDemo.print(text);
            Funcion<String> func = MethodReferenceDemo::print;//上記と同じ処理
            
            func.apply("Hello, World!");
        }
    }
    
    public interface GetInterface<T> {
        T get();
        default T get2(){
            return (T)"get2";
        };
    }
    
    public interface DoSomethingInterface<T> {
        void doSomething();
    }
    
    public static class SampleClass {
        private IntToStringInterface functionalInterface = value -> "value is" + value;//実行されない
        private void process(int arg) {
            functionalInterface = value -> 
                value < 10 
                ? functionalInterface.convert(++value)
                : String.valueOf(value);
            System.out.println("結果:" + functionalInterface.convert(arg));
        }
        public static void main(String... args) {
            SampleClass sample = new SampleClass();
            // 10より小さい場合
            sample.process(0);
            // 10以上の場合
            sample.process(11);
        }
    
        @FunctionalInterface
        public interface IntToStringInterface {
                String convert(int value);
        }
    }
    
    private static void isNumberInterfaceExec(String str){
        IsNumberInterface isNumberInterface = value -> {
            try {
                new BigDecimal(value);
                return true;
            } catch (Exception e) {
                return false;
            }
        };
        System.out.println("isNumberInterfaceExec : " + str + " : " + isNumberInterface.check(str));
    }
    
    @FunctionalInterface
    public interface IsNumberInterface {
        boolean check(String value);
    }
    
    @FunctionalInterface
    private interface SampleFunction {
        public void say(String name);
    }
    
    //</editor-fold >
    
}
