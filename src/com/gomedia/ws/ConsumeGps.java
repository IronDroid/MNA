package com.gomedia.ws;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class ConsumeGps {

    //private static final String URL = "http://192.168.2.101:8081/AudioGuiasWS/videoguia?WSDL";
    private static final String URL = "http://200.87.225.157:1111/AudioGuiasWS/videoguia?WSDL";
    private static final String NAMESPACE = "http://ws.videoguia.mna/";
    private static final String METHOD_LOGIN = "guiaPosicion";
    private static final String SOAP_ACTION_LOGIN = "http://ws.videoguia.mna/videoguia/guiaPosicionRequest";
    //private ObjRespuesta resp;
    private SoapObject request = null;
    private SoapSerializationEnvelope envelope = null;
    private SoapObject resultsRequestSOAP = null;

    public String gpsIn(String imei, String latitud, String longitud) throws Exception {
//        resp = new ObjRespuesta();

        Log.i("-->INGRESAMOSSSSSSSS", "-->INGRESAMOSSSSSSSS");

        String respuesta = "--";
        request = new SoapObject(NAMESPACE, METHOD_LOGIN);
        request.addProperty("imei", imei);
        request.addProperty("latitud", latitud);
        request.addProperty("longitud", longitud);


        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);
        transporte.call(SOAP_ACTION_LOGIN, envelope);
        Log.i("-->INGRESAMOSSSSSSSS", "-->INGRESAMOSSSSSSSS");
        //resultsRequestSOAP = (SoapObject) envelope.getResponse();

        SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();

        //resp.setCodigo(Integer.parseInt(resultsRequestSOAP.getProperty("codigo").toString()));
        //resp.setMensaje(resultsRequestSOAP.getProperty("descripcion").toString());

        Log.i("-->INGRESAMOSSSSSSSS nro=" + resultado, "-->INGRESAMOSSSSSSSS nro=" + resultado);
        //respuesta = (String) resultsRequestSOAP.toString();
        respuesta = resultado.toString();
        Log.i("-->" + respuesta, "-->" + respuesta);
        //return resp;
        return respuesta;
    }
}
