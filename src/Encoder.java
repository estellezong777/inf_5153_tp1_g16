// Un class abstract, il peut encoder/décoder des messages qui sont envoyés/reçus par les agents
// selon un algorithme d'encodage/décodage dans ses sous-classes
public abstract class Encoder {
    public abstract String encode(String msg);
    public abstract String decode(String msg);
}