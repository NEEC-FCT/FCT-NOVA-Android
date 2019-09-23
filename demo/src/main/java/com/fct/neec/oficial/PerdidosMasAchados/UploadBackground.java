package com.fct.neec.oficial.PerdidosMasAchados;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadBackground extends AsyncTask<String, Void, String> {

    String upLoadServerUri = "https://fcthub.neec-fct.com/PHP/uploadPerdidos.php";

        @Override
        protected String doInBackground(String... params) {


            String sourceFileUri = params[0];
            String nome = params[1];
            String local = params[2];
            String  descricao = params[3];
            String categoria = params[4];

                String fileName = sourceFileUri;

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);

                if (!sourceFile.isFile()) {

                    return  "Source File not exist :" ;
                }
                else
                {
                    int serverResponseCode;
                    try {

                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Open a HTTP  connection to  the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("fileToUpload", fileName);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"fileToUpload\";filename="+ fileName + "" + lineEnd);
                        dos.writeBytes(lineEnd);

                        // create a buffer of  maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        }

                        // send multipart form data necesssary after file data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary  + lineEnd);

                        //Send parameters
                        //Nome
                        dos.writeBytes("Content-Disposition: form-data; name=\"nome\";" + lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(nome + lineEnd);
                        //local
                        dos.writeBytes(twoHyphens + boundary  + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"local\";" + lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(local + lineEnd);
                        //descricao
                        dos.writeBytes(twoHyphens + boundary  + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"descricao\";" + lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(descricao + lineEnd);
                        dos.writeBytes(twoHyphens + boundary  + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"categoria\";" + lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(categoria + lineEnd);
                        //Fim do Pedido
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                        // Responses from the server (code and message)
                        serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn.getResponseMessage();

                        Log.i("uploadFile", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);

                        if(serverResponseCode == 200){

                            return  "File Upload Complete.";
                        }

                        //close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (MalformedURLException ex) {


                        ex.printStackTrace();



                        return "error: " + ex.getMessage() ;
                    } catch (Exception e) {


                        e.printStackTrace();

                        return "Exception : "
                                + e.getMessage();
                    }

                    return  String.valueOf( serverResponseCode);

                } // End else block
        }

        @Override
        protected void onPostExecute(String result) {
            PerdidosEAchados.uploadDone();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}



}
