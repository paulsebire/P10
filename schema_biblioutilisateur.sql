PGDMP         8            	    x           biblioutilisateur    12.0    12.0     
           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    76275    biblioutilisateur    DATABASE     �   CREATE DATABASE biblioutilisateur WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';
 !   DROP DATABASE biblioutilisateur;
                postgres    false            �            1259    116833    hibernate_sequence    SEQUENCE     {   CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.hibernate_sequence;
       public          postgres    false            �            1259    116835    roles    TABLE     ]   CREATE TABLE public.roles (
    id_user bigint NOT NULL,
    roles character varying(255)
);
    DROP TABLE public.roles;
       public         heap    postgres    false            �            1259    116838    users    TABLE     �  CREATE TABLE public.users (
    id_user bigint NOT NULL,
    account_non_expired boolean,
    account_non_locked boolean,
    credentials_non_expired boolean,
    email character varying(255) NOT NULL,
    enabled boolean,
    firstname character varying(255) NOT NULL,
    lastname character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false            �
           2606    116848 "   users uk_r43af9ap4edm43mmtq01oddj6 
   CONSTRAINT     a   ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);
 L   ALTER TABLE ONLY public.users DROP CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6;
       public            postgres    false    204            �
           2606    116845    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id_user);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    204            �
           1259    116846    index_user_role    INDEX     D   CREATE INDEX index_user_role ON public.roles USING btree (id_user);
 #   DROP INDEX public.index_user_role;
       public            postgres    false    203            �
           2606    116849 !   roles fk40d4m5dluy4a79sk18r064avh    FK CONSTRAINT     �   ALTER TABLE ONLY public.roles
    ADD CONSTRAINT fk40d4m5dluy4a79sk18r064avh FOREIGN KEY (id_user) REFERENCES public.users(id_user);
 K   ALTER TABLE ONLY public.roles DROP CONSTRAINT fk40d4m5dluy4a79sk18r064avh;
       public          postgres    false    2696    204    203           