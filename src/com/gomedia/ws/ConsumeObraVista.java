package com.gomedia.ws;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class ConsumeObraVista {

    //private static final String URL = "http://192.168.2.101:8081/AudioGuiasWS/videoguia?WSDL";
    private static final String URL = "http://200.87.225.157:1111/AudioGuiasWS/videoguia?WSDL";
    private static final String NAMESPACE = "http://ws.videoguia.mna/";
    private static final String METHOD_LOGIN = "guiaObraVista";
    private static final String SOAP_ACTION_LOGIN = "http://ws.videoguia.mna/videoguia/guiaObraVistaRequest";
    private SoapObject request = null;
    private SoapSerializationEnvelope envelope = null;

    public String obraVisualizadaIn(String imei, String codigoObraVista, String tipo) throws Exception {

        String respuesta = "--";
        request = new SoapObject(NAMESPACE, METHOD_LOGIN);
        request.addProperty("imei", imei);
        request.addProperty("codigoObraVisto", codigoObraVista);
        request.addProperty("tipo", tipo);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);
        transporte.call(SOAP_ACTION_LOGIN, envelope);

        SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();

        respuesta = resultado.toString();

        Log.i("-->" + respuesta, "-->" + respuesta);
        return respuesta;
    }
}
