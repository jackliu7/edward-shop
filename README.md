# edward-shop
## 商家企业ERP系统电商端商家入驻
本系统为了满足易扩展、高并发，抛弃了传统的单一架构，使用了面向服务的SOA架构，利用Dubbo+Zookeeper对原来的单一架构进行切分，原来的表现层、服务层、持久层都在一起一个项目中，现在把这三层分开，表现层和服务层之间使用Dubbo调用。服务层是服务的提供者，把服务注册到Zookeeper，表现层负责处理前段的请求，通过Zookeeper获取服务的地址，利用Dubbo远程调用服务，返回给前端数据，渲染页面，每一个服务采用Spring、SpringMVC、Mybatis完成主要功能。

表现层：表现层主要是负责和前端交互，根据需求分析，把前端分为商家后台、运营商后台、网站首页、搜索页、商品详细页、用户中心、CAS登录中心、购物车8大部分，其中商家后台和运营商后台是单独存在，其他6个部分是从网站前台划分来的，这六个部分之间是相互联系的，用户首先进入网站首页，然后再搜索框输入关键字搜索后来到搜索频道，在搜索频道搜索到详细商品后，点击商品进入商品详细页，加入购物车后，进入购物车部分，提交订单后，进入用户中心。每个表现层都通过Dubbo+Zookeeper调用服务层的方法，返回数据给页面。

服务层：服务层主要有7大服务构成分别为商家商品服务、广告内容服务、页面生成服务、搜索服务、购物车服务、订单服务、用户服务，服务层是三层架构中最重要的一层，负责处理业务逻辑，从Redis缓存或者MySQL中取出数据，返回给表现层。本架构最大的特点就是把服务层提出了，把服务进行了划分，传统架构中商家后台和运营商后台都需要调用商家商品服务，但这是两个不同的系统，就需要在每个系统中都写一个商家商品服务，这样就造成了代码的冗余，这是采用分布式架构，不同的系统都可以调用一个服务，这分时布式架构的优点就体现出来了。

数据层：数据层也叫持久层，是架构中的最底层，负责处理数据，本系统的数据根据需求的不同，分别用到了MySQL、Redis、Solr。系统的大量数据都存放在MySQL中，但在一些频繁访问的页面，为了让系统响应速度更快，就把数据存放在Redis缓存中，这样就可以不用再去访问数据了。网站前台有个重要的功能就是搜索，用户想买的东西都是通过搜索找到的，Solr是专门解决搜索问题的，把商品的一些关键信息存放到Solr的索引库中，前端搜索的数据就不用去找MySQL，性能大大提升。
系统中有一些比较耗时的操作，比如页面生成服务，运营商审核商品后，会生成商品的详细页，这是可以把商品的ID放入ActiveMQ消息队列中，这样就可以慢慢的生成，对系统没有丝毫的影响，而且提升了用户的使用体验。
