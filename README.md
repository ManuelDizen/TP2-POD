# TP2-POD
Repositorio para el trabajo práctico N°1 de la materia "72.42 - Programación de Objetos Distribuidos" - ITBA

## 1. Compilar el proyecto
Siguiendo los pasos provistos por la cátedra en el archivo *Hazelcast - Configuración Java*, es necesario primero construir el proyecto corriendo:

```bash
mvn clean install
```
En la raíz del proyecto encontrarán archivos .sh usados para correr el servidor y las consultas.

## 2. Correr el servidor
```bash
./runserver.sh
```
## 3. Correr los clientes de las queries
```bash
./queryX.sh -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DinPath=XX -DoutPath=YY [params]
```
Donde
- **queryX** es el script que corre la query X.
- **Daddresses** refiere a las direcciones IP de los nodos con sus puertos (una o más, separadas por punto y coma)
- **DinPath** indica el path donde están los archivos de entrada bikes.csv y stations.csv.
- **DoutPath** indica el path donde estarán ambos archivos de salida.
- **params** son los parámetros extras que corresponden para algunas queries.

Las queries 1 y 3 se corren sin parametros adicionales, mientras que la query 2 utiliza un _-Dn=n_ (numero entero) y la
y la query 4 utiliza _-DstartDate=dd/MM/yyyy_ y _-DendDate=dd/MM/yyyy_.

### Observaciones
- En la consigna se pedía que los clientes corrieran desde terminal sin incluir la extensión `.sh` (Ejemplo: `./query1 ...`). Esto depende de la configuración de la terminal, pero en el caso de este desarrollo (y como está aclarado en este README), el proyecto se corrió con la extensión incluida.

## Integrantes:
| Nombre                                                | Legajo |
|-------------------------------------------------------|--------|
| [De Simone, Franco](https://github.com/desimonef)                              | 61100  |
| [Dizenhaus, Manuel](https://github.com/ManuelDizen)                              | 61101  |
| Anselmo, Sol                                          | 61278  |
