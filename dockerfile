FROM maven:3.5-jdk-8

# install node/npm
RUN curl -sL https://deb.nodesource.com/setup_8.x | bash && apt-get install nodejs

# install yarn only to install angular cli with it, using npm caused trouble ...
RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add - \
&& echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list \
&& apt-get update && apt-get install yarn
RUN yarn global add @angular/cli npm

# reading project files into the image
RUN mkdir /ajs && cd /ajs
ADD . /ajs

# Update node modules
RUN cd /ajs/client && npm update

#Build Distribution
RUN cd /ajs && mvn package

#Extract distribution
RUN mkdir /ajs-server && tar xvzf /ajs/distribution/target/distribution-0.0.1-SNAPSHOT-distribution.tar.gz -C /ajs-server

CMD ["/ajs-server/bin/ajs.sh"]
WORKDIR /ajs-server
VOLUME ["/ajs-server"]