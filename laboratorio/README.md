# README.MD
Este proyecto es un laboratorio para consumir el API REST de Mercado Libre, especificamente el recurso itemsItemIdGet. 

## Contenido
Este proyecto actualmente contiene una branch principal donde se desarrollo este Demo.

## Demo
No cuenta con GUI, solo se desarrollo la parte back, así que para consumirlo es necesario usar una herramienta donde pueda construir el request como se especifica más adelante.
Está desplegado en el sitio web [Demo](http://labcoupon-env.eba-3nhetxu2.us-east-2.elasticbeanstalk.com/)

## Ejecucion

Para la ejecución se expone este recurso http://labcoupon-env.eba-3nhetxu2.us-east-2.elasticbeanstalk.com/coupon que debe ser consumido con el método ## POST.

El body debe contener un Json con dos valores, item_ids y total.

    ## item_ids
        Es una lista de los ids que se quieren procesar
    
    ## total
        es el total de dinero que se tiene para consumir la lista de items

## Ejemplo Request Post

    http://labcoupon-env.eba-3nhetxu2.us-east-2.elasticbeanstalk.com/coupon

    Body:

    {
    "item_ids": ["MCO450978136", "MCO565110179", "MCO562867529", "MCO453221363", "MCO455834206"],
    "amount": 450000
    }

## Ejemplo Response

    {
        "item_ids": [
            "MCO450978136",
            "MCO455834206",
            "MCO562867529"
        ],
        "total": 437990
    }

