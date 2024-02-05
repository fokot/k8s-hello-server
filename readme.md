# Project for testing k8s locally

It has:
* config map
* secret
* hostPath (if you run this on mac, this is path on the virtual manchine system, not on your mac)


To test run
```
docker build . -f Dockerfile --tag hello-server:latest
kubectl apply -f deployment.yaml
kubectl port-forward -n default svc/hello-server 8080:8080
open http://locahost:8080
```