Execute following command from Powershell
```
docker run -it --rm -v "${PWD}:/certs" -w /certs alpine:latest sh -c "apk update && apk add openssl && sh"
```

Execute the follwoing for client, server and CA certificates and keys

```dockerfile

# Step 3: Generate the Server Certificate and Key
openssl req -newkey rsa:2048 -nodes -keyout server.key -x509 -days 365 -out server.crt

# Step 4: Generate the Client CA Certificate and Key
openssl req -newkey rsa:2048 -nodes -keyout client-ca.key -x509 -days 365 -out client-ca.crt

# Step 5: Generate the Client Certificate Signing Request (CSR)
openssl req -newkey rsa:2048 -nodes -keyout client.key -out client.csr

# Step 6: Sign the Client CSR with the Client CA
openssl x509 -req -in client.csr -CA client-ca.crt -CAkey client-ca.key -CAcreateserial -out client.crt -days 365

```