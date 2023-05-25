The index server and the peer-to-peer processes are implemented separately. The roles of these two processes are as below
Master Index-Server Process: This is a TCP concurrent server. This process will allow a client to login and request a file/media. The Index server identifies the location of the resource that the client is looking to download and shares the IP and port number of the server holding that file.
Peer-to-peer process: This process has 2 child threads. One thread acts as a client and the other acts as a music/file server. The Client makes a connection to the Index server and places a request. Once the request is responded to, the client acts accordingly. If the file exists, the client makes a new connection with the server and downloads the file. If the file does not exist, i.e., none of the servers registered on the index server has the file requested by the client, the client exits gracefully.


Supporting Files
•	usrList.txt
This file is used to store user credentials. The usernames and passwords are separated by a semicolon. The index server uses this file to validate user logins.

•	contentList.txt
This file is used to store file/media content information. The information is stored like tuples in the text file separated by a semicolon. The information includes the file name, type, availability, IP, and port number. The index server uses this file to check for the files requested by clients and shares the information about the file if found in the file.
