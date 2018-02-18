package ServerExchange.ServerRequests;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Created by Dryush on 13.02.2018.
 *
 * Как этим пользоваться:
 * Наследуешь новый класс от этого, указывая в "<>" имя класса, объект которого хочешь получить от сервера
 * Наследуешь от внутренний класс от класса JsonServerAnswer( здесь будем его называть JSC),
 *          в которым оставляешь открытые поля. Имена полей должны соответствовать
 *          именам в JSON-ответе серевера (см. server_API  в папке сервера)
 * Реализуешь у JSC метод convert(), в котором пишешь создание желаемого объекта на основе полученных данных
 * Реализуешь метод основного класса getMethod,
 *      в котором создаешь объект метода, указывая его имя, имена параметров и значения и возвращаешь его
 * Реализуешь метод основного класса getJsonAnswerClass() его код: {JSC.getClass()}
 * Реализуешь метод getRequestType(), возвращающий тип запроса GET или POST
 *
 * На этом должно взлететь
 *
 * Чтоб воспользоваться любым из наследников достаточно создать объект этого класса
 * Вызвать метод startRequest, в который передать объект, реализующий интерфейс IServerAnswerHandler
 * В методе handle() этого класса реализовать то, что надо сделать с ответом от сервера
 */



public abstract class ServerRequest <AnswerType> {
    //private final String API = "api?method=";
    private final String API = "api?";



    private static String defaultServerAddress = "localhost/";
    private String serverAddress;
    private final String PROTOCOL = "http://";

    enum RequestType{
        GET,
        POST
    }

    protected RequestType getRequestType(){
        return RequestType.POST;
    }


    public static void setDefaultAddress(String address){
        if (address.charAt( address.length() -1) != '/'){
            address += '/';
        }
        defaultServerAddress = address;
    }


    public ServerRequest(String serverAddress)
    {
        this.serverAddress = serverAddress;
    }
    public ServerRequest(){ serverAddress = defaultServerAddress; }



    protected HashMap<String,String> basicMethodParams(){
        HashMap<String,String> namesAndParams = new HashMap<String,String>();
        return namesAndParams;
    }



    /*
    protected void setBasicMethodParams(ServerMethod met,  String paramsNames[], String paramsValues[]) {
        Collections.addAll(met.paramNames, paramsNames);
        Collections.addAll(met.paramValues, paramsValues);
    }
    */


    protected class ServerMethod{
        String methodName;
        HashMap<String, String> params = new HashMap<>();

        
        public ServerMethod( String methodName, HashMap<String,String> methodParams){
            this.methodName = methodName;
            params.putAll(basicMethodParams());
            params.putAll(methodParams);
        }
    }

    abstract protected ServerMethod getMethod();

    //В этом классе должны быть public поля для записи в них ответа из Json файла
    abstract protected class JsonServerAnswer{
        public String status;
        public String message;
        public boolean isStatusOk() {
            return status == "OK";
        }


        abstract public AnswerType convert();
    }

    abstract protected Class<? extends JsonServerAnswer> getJsonAnswerClass();

    AnswerType answer;



    protected AnswerType doRequest(ServerMethod method, Class< ? extends JsonServerAnswer> JsonServerAnswerClass) throws IOException {

        String request = PROTOCOL + serverAddress + API;
        if ( getRequestType() == RequestType.POST ) {
            request = request + method.methodName;
        }
        else if ( getRequestType() == RequestType.GET){
            request += "method=" + method.methodName;
            Set<String> names = method.params.keySet();
            for (String name : names){
                request = request + '&' + name + "=" + method.params.get(name);
            }
        }

        try {
            URL requestURL = new URL(request);

            HttpURLConnection urlConnection = (HttpURLConnection) requestURL.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            String strRequestMethod = "";
            if (getRequestType() == RequestType.POST) { strRequestMethod = "POST"; }
            else if ( getRequestType() == RequestType.GET) {strRequestMethod = "GET";}
            urlConnection.setRequestMethod(strRequestMethod);

            urlConnection.connect();
            Gson gson = new Gson();

            if (getRequestType() == RequestType.POST) {

                String jsonRequest = gson.toJson(method.params);
                OutputStream out = urlConnection.getOutputStream();
                out.write(jsonRequest.getBytes());
                //ObjectOutputStream objectOut = new ObjectOutputStream( out);
                //objectOut.writeUTF(jsonRequest);
                   out.flush();
                out.close();
            }

            InputStream in = urlConnection.getInputStream();
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inReader);

            //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //String a = reader.readLine();





            JsonServerAnswer serverAnswer = gson.fromJson(reader, JsonServerAnswerClass);
            if ( serverAnswer.status.equals("Error") ){
                throw new ServerExceprion( serverAnswer.message);
            }
            JsonAnswerHandler(serverAnswer);

            return (AnswerType) serverAnswer.convert();

        } catch (Exception except){
            throw except;
        }
    }


    /**
     * перегрузи этот метод, если хочешь обработать JSON ответ до его конвертации
     *
     * @param answ
     */
    protected void JsonAnswerHandler(JsonServerAnswer answ){

    }

    /**
     *
     * Перегрузи этот метод если хочешь обработать конвертированный ответ до того, как вернуть его инциатору запроса
     *
     * @param answ
     */
    protected void ConvertedAnswerHandler( AnswerType answ){

    }
    
    private class RequestProcess extends AsyncTask<Void, Void, Void> {

        IServerAnswerHandler handler;

        Class<? extends JsonServerAnswer> aClass;


        protected RequestProcess(IServerAnswerHandler handler, Class<? extends JsonServerAnswer> jsonAnswerClass)
        {
            this.handler = handler;
            aClass = jsonAnswerClass;
        }

        AnswerType answer;

        @Override
        protected Void doInBackground(Void ... p){
            try {
                answer = doRequest(getMethod(), aClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void p){
            if (handler != null) {

                handler.handle(answer);
            }
        }


    }
    
    
    protected void startRequest(IServerAnswerHandler handler){
        RequestProcess rp = new RequestProcess( handler, getJsonAnswerClass());
        rp.execute();
    }

    protected void startRequest(){
        RequestProcess rp = new RequestProcess( null, getJsonAnswerClass());
        rp.execute();
    }

    static MessageDigest md;

    static String encryption(String str){
        String res = str;
        try {
            if (md == null)
                md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());

            byte byteData[] = md.digest();

            //конвертируем байт в шестнадцатеричный формат
            StringBuffer sb = new StringBuffer();
            for (byte aByteData : byteData) {
                sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
            }

            res = sb.toString();

        } catch (NoSuchAlgorithmException except){
            //Не может быть такого
        }
        return res;
    }

}
