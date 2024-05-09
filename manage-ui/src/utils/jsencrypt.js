import JSEncrypt from "jsencrypt/bin/jsencrypt.min";

const publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDNNorgFngK1zjHOnQlIUh5NjOx\n' +
    'ZIiEPZ8Knu6B/IyY0LBRToo1TZC7/nK6j8on/2sBdv5nFuTwlOpW9UL8C4yZJdjT\n' +
    'wYXn5X+wZZsz1RXNI5zjhSXuGeYzF7WhxusKo6zrR6b0IMNg2W016PWU3UkjOXxo\n' +
    'aIGkMN77oIorPP5bHQIDAQAB'

const privateKey = 'MIICXQIBAAKBgQDNNorgFngK1zjHOnQlIUh5NjOxZIiEPZ8Knu6B/IyY0LBRToo1\n' +
    'TZC7/nK6j8on/2sBdv5nFuTwlOpW9UL8C4yZJdjTwYXn5X+wZZsz1RXNI5zjhSXu\n' +
    'GeYzF7WhxusKo6zrR6b0IMNg2W016PWU3UkjOXxoaIGkMN77oIorPP5bHQIDAQAB\n' +
    'AoGABOdOvjgLOkcWRjxxVgnLj4nqBk0erfpC+J//lv+P5H7oF6lGyCtIUBWubCLP\n' +
    'c9E4n1pWjeQQKGeGiflmVlt4So2UPQJD/fvpmT0lswaud+ObbUtFIo4CApHMXdTB\n' +
    'jIC/nDSdFut2Yd32N8OH/QYnzAS1tarLGjk3x+Dg5nY3VEECQQDvM7GLXT2df85I\n' +
    'X+FBX9YiwUPXqciUJp3XdBOngsyENOFu0C3/cBTxvaiKkMXVPqMjOdoCAY+hz/k1\n' +
    'xPUVBpZ5AkEA25/Objru9LI1XSj8M1gJoIUpiR+mJysN7Q7wWbSK6DI+Hz95NQ5r\n' +
    'kAzG89lwMW3dLycH8VPGsWMuxjA7NG0QxQJBAIxDxdKxJFZdAXuTLaWGKy1KIxwt\n' +
    'pT6qvlf+6x+JJaBI2gB+9toYwU9YJaLLbhazmjonzFzsyWrbZ4lOK2De8hECQQCl\n' +
    'uJRgAQBGjCJQRZjodUnuYgzRd5w8efRsKJWcWutmAmN12MNxEYyAieOmJTDPW4NH\n' +
    'DUClDP4k5B5rVgGWsaWxAkA4m0bHwiPqO4/Yz6eyl2jYvljtmqr7KZFXrlsBUrIm\n' +
    'XXaTuMdsOmLlp/u078XFw0N+RaUWxbE6ATH7mTGjB2nV'

export function encrypt(txt){
    const encryptor = new JSEncrypt()
    encryptor.setPublicKey(publicKey)
    return encryptor.encrypt(txt)
}

export function decrypt(txt){
    const encryptor = new JSEncrypt()
    encryptor.setPrivateKey(privateKey)
    return encryptor.decrypt(txt)
}
