PGDMP         8            	    x           biblioutilisateur    12.0    12.0                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    76275    biblioutilisateur    DATABASE     �   CREATE DATABASE biblioutilisateur WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';
 !   DROP DATABASE biblioutilisateur;
                postgres    false            
          0    116838    users 
   TABLE DATA           �   COPY public.users (id_user, account_non_expired, account_non_locked, credentials_non_expired, email, enabled, firstname, lastname, password, username) FROM stdin;
    public          postgres    false    204   *       	          0    116835    roles 
   TABLE DATA           /   COPY public.roles (id_user, roles) FROM stdin;
    public          postgres    false    203   (                  0    0    hibernate_sequence    SEQUENCE SET     @   SELECT pg_catalog.setval('public.hibernate_sequence', 3, true);
          public          postgres    false    202            
   �   x�Uͻn�0 ��?��-M��(�eJ�i �b�q+����4�����D�m��lx�Z�0�5k���/Q^���MΩ��]Zp�+����&��'�N���!�p��z!U��Hw�e#{B�8�a�@~:D�b睯���{8��L꾒݉f����8�ܒ_�b.��7�f���iS�O��W8��,��(��O��B} ��S�z͎�`ųb'&��˧}���0�\�[M��k� �G�`�      	   !   x�3�v�2�PƜ�.��~@̍���� H�     