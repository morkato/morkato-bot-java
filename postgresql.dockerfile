FROM postgres:17
RUN apt-get update
RUN apt-get install -y \
    meson \
    build-essential \
    ninja-build \
    sudo \
    && rm -rf /var/lib/apt/lists/* \
RUN mkdir /usr/mcisid
COPY mcisid/include /usr/mcisid/include
COPY mcisid/src /usr/mcisid/src
COPY mcisid/meson.build /usr/mcisid/meson.build
COPY mcisid/pgmcisid.control /usr/mcisid/pgmcisid.control
COPY mcisid/pgmcisid--1.0.sql /usr/mcisid/pgmcisid--1.0.sql
WORKDIR /usr/mcisid
RUN sudo meson setup builddir \
    && sudo meson compile -C builddir \
    && sudo meson install -C builddir \
WORKDIR /var/lib/postgresql
CMD ["docker-entrypoint.sh", "postgres"]