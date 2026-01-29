# Architecture - Panier et Liste d'Envie

## Schéma des Relations

```
┌─────────────────┐
│      USER       │
│─────────────────│
│ id              │
│ username        │
│ email           │
│ password        │
│ role            │
└────────┬────────┘
         │
         │ 1
         │
         ├───────────────────────────┐
         │                           │
         │ N                         │ N
         │                           │
┌────────▼────────┐         ┌────────▼────────┐
│   CART_ITEMS    │         │ WISHLIST_ITEMS  │
│─────────────────│         │─────────────────│
│ id              │         │ id              │
│ user_id    (FK) │         │ user_id    (FK) │
│ product_id (FK) │         │ product_id (FK) │
│ quantity        │         │ created_at      │
│ created_at      │         └────────┬────────┘
│ updated_at      │                  │
└────────┬────────┘                  │
         │                           │
         │ N                         │ N
         │                           │
         │ 1                         │ 1
         │                           │
         └───────────┬───────────────┘
                     │
              ┌──────▼──────┐
              │   PRODUCT   │
              │─────────────│
              │ id          │
              │ code        │
              │ name        │
              │ price       │
              │ quantity    │
              │ ...         │
              └─────────────┘
```

## Relations

### User → CartItem (1:N)
- Un utilisateur peut avoir plusieurs produits dans son panier
- Chaque ligne du panier appartient à un seul utilisateur

### User → WishlistItem (1:N)
- Un utilisateur peut avoir plusieurs produits dans sa liste d'envie
- Chaque produit de la wishlist appartient à un seul utilisateur

### Product → CartItem (1:N)
- Un produit peut être dans le panier de plusieurs utilisateurs
- Chaque ligne du panier référence un seul produit

### Product → WishlistItem (1:N)
- Un produit peut être dans la liste d'envie de plusieurs utilisateurs
- Chaque produit de la wishlist référence un seul produit

## Contraintes d'Unicité

### CartItem
```sql
UNIQUE (user_id, product_id)
```
→ Un utilisateur ne peut pas avoir le même produit deux fois dans son panier
→ Pour augmenter la quantité, on modifie la ligne existante

### WishlistItem
```sql
UNIQUE (user_id, product_id)
```
→ Un utilisateur ne peut pas ajouter le même produit deux fois à sa wishlist

## Flux d'Opérations

### 1. Ajouter un Produit au Panier

```
┌─────────────┐
│   CLIENT    │
│ (Frontend)  │
└──────┬──────┘
       │
       │ POST /cart
       │ { "productId": 123, "quantity": 2 }
       │ Authorization: Bearer <JWT>
       │
       ▼
┌─────────────────────────┐
│    BACKEND API          │
│  CartController         │
└──────┬──────────────────┘
       │
       │ 1. Vérifier JWT → obtenir User
       │ 2. Vérifier Product existe
       │ 3. Vérifier si CartItem existe déjà
       │
       ▼
┌─────────────────────────┐
│     CartService         │
└──────┬──────────────────┘
       │
       ├─── SI CartItem existe déjà
       │    → UPDATE quantity = quantity + 2
       │
       └─── SI CartItem n'existe pas
            → INSERT new CartItem(user, product, 2)
       │
       ▼
┌─────────────────────────┐
│      DATABASE           │
│   cart_items table      │
└─────────────────────────┘
```

### 2. Ajouter un Produit à la Wishlist

```
┌─────────────┐
│   CLIENT    │
└──────┬──────┘
       │
       │ POST /wishlist
       │ { "productId": 456 }
       │ Authorization: Bearer <JWT>
       │
       ▼
┌─────────────────────────┐
│    BACKEND API          │
│  WishlistController     │
└──────┬──────────────────┘
       │
       │ 1. Vérifier JWT → obtenir User
       │ 2. Vérifier Product existe
       │ 3. Vérifier si déjà dans wishlist
       │
       ▼
┌─────────────────────────┐
│   WishlistService       │
└──────┬──────────────────┘
       │
       ├─── SI existe déjà
       │    → Retourner erreur 409 Conflict
       │
       └─── SI n'existe pas
            → INSERT new WishlistItem(user, product)
       │
       ▼
┌─────────────────────────┐
│      DATABASE           │
│ wishlist_items table    │
└─────────────────────────┘
```

### 3. Voir son Panier

```
GET /cart
Authorization: Bearer <JWT>

→ Retourne tous les CartItems de l'utilisateur
  avec les infos du produit (JOIN)

Response:
[
  {
    "id": 1,
    "product": {
      "id": 123,
      "name": "Laptop",
      "price": 999.99,
      "image": "..."
    },
    "quantity": 2,
    "subtotal": 1999.98
  },
  ...
]
```

### 4. Voir sa Wishlist

```
GET /wishlist
Authorization: Bearer <JWT>

→ Retourne tous les WishlistItems de l'utilisateur
  avec les infos du produit (JOIN)

Response:
[
  {
    "id": 1,
    "product": {
      "id": 456,
      "name": "Keyboard",
      "price": 79.99,
      "image": "..."
    },
    "createdAt": "2026-01-29T10:00:00"
  },
  ...
]
```

### 5. Modifier la Quantité dans le Panier

```
PATCH /cart/items/{cartItemId}
{ "quantity": 5 }
Authorization: Bearer <JWT>

→ Vérifie que le CartItem appartient à l'utilisateur
→ Met à jour la quantité
→ Si quantity = 0, supprime le CartItem
```

### 6. Supprimer du Panier

```
DELETE /cart/items/{cartItemId}
Authorization: Bearer <JWT>

→ Vérifie que le CartItem appartient à l'utilisateur
→ Supprime le CartItem
```

### 7. Supprimer de la Wishlist

```
DELETE /wishlist/items/{wishlistItemId}
Authorization: Bearer <JWT>

→ Vérifie que le WishlistItem appartient à l'utilisateur
→ Supprime le WishlistItem
```

## APIs à Créer

### CartController
- `POST /cart` - Ajouter un produit au panier
- `GET /cart` - Voir son panier
- `PATCH /cart/items/{id}` - Modifier la quantité
- `DELETE /cart/items/{id}` - Supprimer un article
- `DELETE /cart` - Vider le panier

### WishlistController
- `POST /wishlist` - Ajouter à la wishlist
- `GET /wishlist` - Voir sa wishlist
- `DELETE /wishlist/items/{id}` - Retirer de la wishlist
- `DELETE /wishlist` - Vider la wishlist

## Repositories à Créer

```java
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserId(Long userId);
}

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUserId(Long userId);
    Optional<WishlistItem> findByUserIdAndProductId(Long userId, Long productId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserId(Long userId);
}
```

## Sécurité

✅ Toutes les routes nécessitent un JWT valide
✅ Un utilisateur ne peut accéder qu'à son propre panier/wishlist
✅ Vérification de propriété avant modification/suppression

```java
// Dans le service
User currentUser = getCurrentUserFromJWT();
CartItem item = cartItemRepository.findById(id)
    .orElseThrow(() -> new NotFoundException());
    
if (!item.getUser().getId().equals(currentUser.getId())) {
    throw new ForbiddenException("Not your cart item");
}
```
