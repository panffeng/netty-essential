/*
http://www.cnblogs.com/ggjucheng/archive/2012/01/17/2324974.html

strace -tt  -o straceEpollDirectly.log ./epollserver

$ telnet 127.0.0.1 6888
Trying 127.0.0.1...
Connected to 127.0.0.1.
Escape character is '^]'.
Hello world.
Hello world.

07:46:04.802499 execve("./epollserver", ["./epollserver"], [/ * 22 vars * /]) = 0
07:46:04.802964 brk(0)                  = 0x226d000
07:46:04.803187 access("/etc/ld.so.nohwcap", F_OK) = -1 ENOENT (No such file or directory)
07:46:04.803432 mmap(NULL, 8192, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0) = 0x7f4c342be000
07:46:04.803646 access("/etc/ld.so.preload", R_OK) = -1 ENOENT (No such file or directory)
07:46:04.803868 open("/etc/ld.so.cache", O_RDONLY|O_CLOEXEC) = 3
07:46:04.804083 fstat(3, {st_mode=S_IFREG|0644, st_size=36657, ...}) = 0
07:46:04.804295 mmap(NULL, 36657, PROT_READ, MAP_PRIVATE, 3, 0) = 0x7f4c342b5000
07:46:04.804482 close(3)                = 0
07:46:04.804667 access("/etc/ld.so.nohwcap", F_OK) = -1 ENOENT (No such file or directory)
07:46:04.804861 open("/lib/x86_64-linux-gnu/libc.so.6", O_RDONLY|O_CLOEXEC) = 3
07:46:04.805122 read(3, "\177ELF\2\1\1\0\0\0\0\0\0\0\0\0\3\0>\0\1\0\0\0\320\37\2\0\0\0\0\0"..., 832) = 832
07:46:04.805336 fstat(3, {st_mode=S_IFREG|0755, st_size=1840928, ...}) = 0
07:46:04.805519 mmap(NULL, 3949248, PROT_READ|PROT_EXEC, MAP_PRIVATE|MAP_DENYWRITE, 3, 0) = 0x7f4c33cd9000
07:46:04.805698 mprotect(0x7f4c33e94000, 2093056, PROT_NONE) = 0
07:46:04.805914 mmap(0x7f4c34093000, 24576, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_FIXED|MAP_DENYWRITE, 3, 0x1ba000) = 0x7f4c34093000
07:46:04.806136 mmap(0x7f4c34099000, 17088, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_FIXED|MAP_ANONYMOUS, -1, 0) = 0x7f4c34099000
07:46:04.806174 close(3)                = 0
07:46:04.806204 mmap(NULL, 4096, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0) = 0x7f4c342b4000
07:46:04.806228 mmap(NULL, 8192, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0) = 0x7f4c342b2000
07:46:04.806265 arch_prctl(ARCH_SET_FS, 0x7f4c342b2740) = 0
07:46:04.806556 mprotect(0x7f4c34093000, 16384, PROT_READ) = 0
07:46:04.806588 mprotect(0x601000, 4096, PROT_READ) = 0
07:46:04.806639 mprotect(0x7f4c342c0000, 4096, PROT_READ) = 0
07:46:04.806797 munmap(0x7f4c342b5000, 36657) = 0
07:46:04.806854 setrlimit(RLIMIT_NOFILE, {rlim_cur=10000, rlim_max=10000}) = 0
07:46:04.806904 socket(PF_INET, SOCK_STREAM, IPPROTO_IP) = 3
07:46:04.807102 setsockopt(3, SOL_SOCKET, SO_REUSEADDR, [1], 4) = 0
07:46:04.807131 fcntl(3, F_GETFD)       = 0
07:46:04.807150 fcntl(3, F_SETFL, O_RDONLY|O_NONBLOCK) = 0
07:46:04.807169 bind(3, {sa_family=AF_INET, sin_port=htons(6888), sin_addr=inet_addr("0.0.0.0")}, 16) = 0
07:46:04.807216 listen(3, 1024)         = 0
07:46:04.807422 epoll_create(10000)     = 4
07:46:04.807450 epoll_ctl(4, EPOLL_CTL_ADD, 3, {EPOLLIN|EPOLLET, {u32=3, u64=3}}) = 0
07:46:04.807483 fstat(1, {st_mode=S_IFCHR|0620, st_rdev=makedev(136, 1), ...}) = 0
07:46:04.807507 mmap(NULL, 4096, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0) = 0x7f4c342bd000
07:46:04.807537 write(1, "epollserver startup,port 6888, m"..., 72) = 72
07:46:04.807693 epoll_wait(4, {{EPOLLIN, {u32=3, u64=3}}}, 1, -1) = 1
07:46:31.462586 accept(3, {sa_family=AF_INET, sin_port=htons(56959), sin_addr=inet_addr("127.0.0.1")}, [16]) = 5
07:46:31.462640 write(1, "1:accept form 127.0.0.1:32734\n", 30) = 30
07:46:31.462667 fcntl(5, F_GETFD)       = 0
07:46:31.462686 fcntl(5, F_SETFL, O_RDONLY|O_NONBLOCK) = 0
07:46:31.462706 epoll_ctl(4, EPOLL_CTL_ADD, 5, {EPOLLIN|EPOLLET, {u32=5, u64=5}}) = 0
07:46:31.462728 epoll_wait(4, {{EPOLLIN, {u32=5, u64=5}}}, 2, -1) = 1
07:46:43.226369 read(5, "Hello world.\r\n", 10240) = 14
07:46:43.226409 write(5, "Hello world.\r\n", 14) = 14
07:46:43.226439 epoll_wait(4, 7fff7c35da10, 2, -1) = -1 EINTR (Interrupted system call)
07:47:01.860999 --- SIGINT {si_signo=SIGINT, si_code=SI_KERNEL} ---
07:47:01.861118 +++ killed by SIGINT +++



*/

#include  <unistd.h>
#include  <sys/types.h>       /* basic system data types */
#include  <sys/socket.h>      /* basic socket definitions */
#include  <netinet/in.h>      /* sockaddr_in{} and other Internet defns */
#include  <arpa/inet.h>       /* inet(3) functions */
#include <sys/epoll.h> /* epoll function */
#include <fcntl.h>     /* nonblocking */
#include <sys/resource.h> /*setrlimit */

#include <stdlib.h>
#include <errno.h>
#include <stdio.h>
#include <string.h>



#define MAXEPOLLSIZE 10000
#define MAXLINE 10240
int handle(int connfd);
int setnonblocking(int sockfd)
{
    if (fcntl(sockfd, F_SETFL, fcntl(sockfd, F_GETFD, 0)|O_NONBLOCK) == -1) {
        return -1;
    }
    return 0;
}

int main(int argc, char **argv)
{
    int  servPort = 6888;
    int listenq = 1024;

    int listenfd, connfd, kdpfd, nfds, n, nread, curfds,acceptCount = 0;
    struct sockaddr_in servaddr, cliaddr;
    socklen_t socklen = sizeof(struct sockaddr_in);
    struct epoll_event ev;
    struct epoll_event events[MAXEPOLLSIZE];
    struct rlimit rt;
    char buf[MAXLINE];

    /* 设置每个进程允许打开的最大文件数 */
    rt.rlim_max = rt.rlim_cur = MAXEPOLLSIZE;
    if (setrlimit(RLIMIT_NOFILE, &rt) == -1)
    {
        perror("setrlimit error");
        return -1;
    }


    bzero(&servaddr, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htonl (INADDR_ANY);
    servaddr.sin_port = htons (servPort);

    listenfd = socket(AF_INET, SOCK_STREAM, 0);
    if (listenfd == -1) {
        perror("can't create socket file");
        return -1;
    }

    int opt = 1;
    setsockopt(listenfd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    if (setnonblocking(listenfd) < 0) {
        perror("setnonblock error");
    }

    if (bind(listenfd, (struct sockaddr *) &servaddr, sizeof(struct sockaddr)) == -1)
    {
        perror("bind error");
        return -1;
    }
    if (listen(listenfd, listenq) == -1)
    {
        perror("listen error");
        return -1;
    }
    /* 创建 epoll 句柄，把监听 socket 加入到 epoll 集合里 */
    kdpfd = epoll_create(MAXEPOLLSIZE);
    ev.events = EPOLLIN | EPOLLET;
    ev.data.fd = listenfd;
    if (epoll_ctl(kdpfd, EPOLL_CTL_ADD, listenfd, &ev) < 0)
    {
        fprintf(stderr, "epoll set insertion error: fd=%d\n", listenfd);
        return -1;
    }
    curfds = 1;

    printf("epollserver startup,port %d, max connection is %d, backlog is %d\n", servPort, MAXEPOLLSIZE, listenq);

    for (;;) {
        /* 等待有事件发生 */
        nfds = epoll_wait(kdpfd, events, curfds, -1);
        if (nfds == -1)
        {
            perror("epoll_wait");
            continue;
        }
        /* 处理所有事件 */
        for (n = 0; n < nfds; ++n)
        {
            if (events[n].data.fd == listenfd)
            {
                connfd = accept(listenfd, (struct sockaddr *)&cliaddr,&socklen);
                if (connfd < 0)
                {
                    perror("accept error");
                    continue;
                }

                sprintf(buf, "accept form %s:%d\n", inet_ntoa(cliaddr.sin_addr), cliaddr.sin_port);
                printf("%d:%s", ++acceptCount, buf);

                if (curfds >= MAXEPOLLSIZE) {
                    fprintf(stderr, "too many connection, more than %d\n", MAXEPOLLSIZE);
                    close(connfd);
                    continue;
                }
                if (setnonblocking(connfd) < 0) {
                    perror("setnonblocking error");
                }
                ev.events = EPOLLIN | EPOLLET;
                ev.data.fd = connfd;
                if (epoll_ctl(kdpfd, EPOLL_CTL_ADD, connfd, &ev) < 0)
                {
                    fprintf(stderr, "add socket '%d' to epoll failed: %s\n", connfd, strerror(errno));
                    return -1;
                }
                curfds++;
                continue;
            }
            // 处理客户端请求
            if (handle(events[n].data.fd) < 0) {
                epoll_ctl(kdpfd, EPOLL_CTL_DEL, events[n].data.fd,&ev);
                curfds--;


            }
        }
    }
    close(listenfd);
    return 0;
}
int handle(int connfd) {
    int nread;
    char buf[MAXLINE];
    nread = read(connfd, buf, MAXLINE);//读取客户端socket流

    if (nread == 0) {
        printf("client close the connection\n");
        close(connfd);
        return -1;
    }
    if (nread < 0) {
        perror("read error");
        close(connfd);
        return -1;
    }
    write(connfd, buf, nread);//响应客户端
    return 0;
}