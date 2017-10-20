## > sudo pip3 install grpcio-tools
## > python3 -m grpc_tools.protoc -I../proto --python_out=. --grpc_python_out=. ../proto/helloworld.proto

import grpc
import helloworld_pb2_grpc
import helloworld_pb2

channel = grpc.insecure_channel("localhost:50051")
stub = helloworld_pb2_grpc.GreeterStub(channel)
request = helloworld_pb2.HelloRequest(name="Python")

## Call it!!!
response = stub.SayHello(request)
print("Got {} from server".format(response.message))
