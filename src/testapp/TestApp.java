/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
        hatenaSample2();
        hatenaSample3();
    }
    
    private static void hatenaSample1(){
        String name = new Throwable().getStackTrace()[0].getMethodName();
        List<String> names = Arrays.asList("hoge hoge", "foo bar", "naoki", "kishida");
        names.stream()
                .map(s -> name + "-1: [" + s + "]")
                .filter(s -> s.length() > 5)
                .forEach(System.out::println);
        names.stream()
                .filter(s -> s.length() > 5)
                .map(s -> name + "-2: [" + s + "]")
                .forEach(System.out::println);
    }
    
    private static void hatenaSample2(){
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        List<String> names = Arrays.asList("hoge hoge", "foo bar", "naoki", "kishida");
        List<String> names2 = Arrays.asList("12", "3 5");
        
        System.out.println(methodName + ":start ----------------");
        System.out.println(names.stream().allMatch(s -> !s.isEmpty())); //true
        System.out.println(names.stream().anyMatch(s -> s.length() > 7)); //true
        System.out.println(names.stream().noneMatch(s -> s.startsWith("A"))); //true
        
        //System.out.println(names.stream().collect(Collectors.toStringJoiner(":")) );//err
        StringJoiner sj = new StringJoiner(":", "[", "]");
        names.stream().forEach(s -> sj.add(s));
        System.out.println(sj.toString());//1行で書けないか
        
        System.out.println(names2.stream().collect(Collectors.summingLong(s -> s.length())));
        
        System.out.println(methodName + ":end ----------------");
    }
    
    private static void hatenaSample3(){
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
