// L’encodeur neutre ne fait aucune transformation du texte
public class EncoderNeutre extends Encoder{
    @Override
    public String encode (String msg){
        return msg;
    }

    public String decode(String msg){
        return msg;
    }

}
