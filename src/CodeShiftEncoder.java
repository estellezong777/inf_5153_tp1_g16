public class CodeShiftEncoder extends Encoder {
    @Override
    public String encode(String msg) {
        StringBuilder encodedMessage = new StringBuilder();
        for (char c : msg.toCharArray()) {
            encodedMessage.append((char) (c + 1));
        }
        return encodedMessage.toString();
    }

    @Override
    public String decode(String msg) {
        StringBuilder decodedMessage = new StringBuilder();
        for (char c : msg.toCharArray()) {
            decodedMessage.append((char) (c - 1));
        }
        return decodedMessage.toString();
    }
}
