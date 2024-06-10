from flask import Flask, request, jsonify
import ssl

app = Flask(__name__)

# Configure Flask app to require client certificates
context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
context.load_cert_chain('server.crt', 'server.key')
context.verify_mode = ssl.CERT_REQUIRED
context.load_verify_locations(cafile='client-ca.crt')

# Predefined valid client_id and client_secret for demonstration purposes
VALID_CLIENT_ID = "my_client_id"
VALID_CLIENT_SECRET = "my_client_secret"

@app.route('/token', methods=['POST'])
def token():
    # Extract client certificate from request
    cert = request.environ.get('SSL_CLIENT_CERT')

    # Validate client certificate
    if not cert:
        return jsonify(error="Client certificate not provided"), 401

    # Extract client_id and client_secret from request body
    data = request.json
    client_id = data.get('client_id')
    client_secret = data.get('client_secret')

    # Validate client_id and client_secret
    if client_id != VALID_CLIENT_ID or client_secret != VALID_CLIENT_SECRET:
        return jsonify(error="Invalid client_id or client_secret"), 401

    # If certificate and credentials are valid, proceed with token generation
    return jsonify(access_token="mock_access_token")

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, ssl_context=context)