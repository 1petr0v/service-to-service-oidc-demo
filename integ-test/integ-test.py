# ad-hoc integ test

import http.client
import time

testMatrix = [
    ("localhost:10001", "/resource1"), # service1:resource1 not allowed without authz
    ("localhost:10001", "/resource2"), # service1:resource2 not allowed without authz
    ("localhost:10002", "/resource1"), # service2:resource1 not allowed without authz
    ("localhost:10002", "/resource2"), # service2:resource2 not allowed without authz
    ("localhost:10002", "/resource1"), # service3:resource1 not allowed without authz
    ("localhost:10002", "/resource2"), # service3:resource2 not allowed without authz
    ("localhost:10001", "/public/jump/service2/resource1"), # service1 -> service2:resource1 - not allowed
    ("localhost:10001", "/public/jump/service2/resource2"), # service1 -> service2:resource2 - allowed
    ("localhost:10001", "/public/jump/service3/resource1"), # service1 -> service3:resource1 - not allowed
    ("localhost:10001", "/public/jump/service3/resource2"), # service1 -> service3:resource2 - allowed
    ("localhost:10002", "/public/jump/service1/resource1"), # service2 -> service1:resource1 - not allowed
    ("localhost:10002", "/public/jump/service1/resource2"), # service2 -> service1:resource2 - allowed
    ("localhost:10002", "/public/jump/service3/resource1"), # service2 -> service3:resource1 - allowed
    ("localhost:10002", "/public/jump/service3/resource2"), # service2 -> service3:resource2 - not allowed
    ("localhost:10003", "/public/jump/service1/resource1"), # service3 -> service1:resource1 - allowed
    ("localhost:10003", "/public/jump/service1/resource2"), # service3 -> service1:resource2 - not allowed
    ("localhost:10003", "/public/jump/service2/resource1"), # service3 -> service2:resource1 - allowed
    ("localhost:10003", "/public/jump/service2/resource2")  # service3 -> service2:resource2 - not allowed
]

for endpoint in testMatrix:
    print("Calling " + endpoint[0] + endpoint[1])
    conn = http.client.HTTPConnection(endpoint[0])
    conn.request("GET", endpoint[1])
    response = conn.getresponse()
    print("Got response status: {}, body: {}".format(response.status, response.read()))
    time.sleep(0.5)
