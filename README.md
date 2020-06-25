# POBJ Arc 4 : Éditeur graphique (TME 9, 10 et 11)

### Support de TME du cours programmation par objets (LU3IN002), Licence 3, Sorbonne Université, Paris, France.

But du projet:

Le but du projet est de développer un éditeur de dessins vectoriels, sur le modèle d’Adobe Illustrator
ou Inkscape. Bien sûr, notre éditeur ne possédera qu’un nombre très réduit de fonctionnalités.
Nous compensons cela en mettant l’accent sur une conception extensible, permettant d’ajouter
facilement des fonctionnalités. La conception de l’éditeur sera basée sur les principes de délégation,
de programmation vis-à-vis d’interfaces et l’emploi de quelquesdesign patterns. Pour la partie
interface graphique (GUI), nous utiliserons l’API JavaFX.

Quelques points clés de notre éditeur :

* séparation entre modèle de document (représentation du dessin sous forme d’objets Java) et
interface graphique d’édition ;

* possibilité d’ouvrir simultanément plusieurs fenêtres, contenant chacune un dessin différent ;

* barre de boutons, menus déroulants, palette de couleurs (en bonus) ;

* ajout de rectangles, d’ellipses, d’images ;

* outil de sélection, simple ou multiple ;

* déplacer, grouper, dégrouper des éléments ;

* copier et coller avec un presse-papiers partagé entre les fenêtres ;

* annuler et refaire une action jusqu’à un niveau arbitraire ;

* sauvegarde du dessin dans un fichier et relecture du fichier .

Pour plus de details, voir le fichier: tme.pdf
