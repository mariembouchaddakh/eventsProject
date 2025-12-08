# 📝 Guide étape par étape : Créer et pousser vers GitHub

## Étape 1 : Créer le dépôt sur GitHub

1. **Allez sur GitHub.com** et connectez-vous
2. Cliquez sur votre **avatar** (en haut à droite) → **"Your repositories"**
3. Cliquez sur le bouton vert **"New"** (ou le bouton **"+"** → **"New repository"**)
4. Remplissez le formulaire :
   - **Repository name** : `eventsProject` (ou le nom que vous voulez)
   - **Description** (optionnel) : "Events Project - Spring Boot avec CI/CD"
   - **Visibility** : Choisissez **Public** ou **Private**
   - ⚠️ **IMPORTANT** : **NE COCHEZ PAS** les cases :
     - ❌ "Add a README file"
     - ❌ "Add .gitignore"
     - ❌ "Choose a license"
5. Cliquez sur **"Create repository"**

## Étape 2 : Copier l'URL du dépôt

Après la création, GitHub vous montre une page avec des instructions. 
**Copiez l'URL HTTPS** qui ressemble à :
```
https://github.com/VOTRE_USERNAME/eventsProject.git
```

## Étape 3 : Lier votre dépôt local à GitHub

Dans votre terminal PowerShell, exécutez ces commandes (remplacez l'URL par celle que vous avez copiée) :

```powershell
# Ajouter le remote GitHub
git remote add origin https://github.com/VOTRE_USERNAME/eventsProject.git

# Vérifier que c'est bien ajouté
git remote -v

# Ajouter le fichier PUSH-TO-GITHUB.md s'il n'est pas encore commité
git add PUSH-TO-GITHUB.md
git commit -m "Ajout du guide GitHub"

# Pousser vers GitHub
git push -u origin main
```

## 🔐 Si GitHub demande une authentification

### Option A : Personal Access Token (Recommandé)

1. Allez sur GitHub → **Settings** (votre profil)
2. Dans le menu de gauche : **Developer settings**
3. **Personal access tokens** → **Tokens (classic)**
4. Cliquez sur **"Generate new token"** → **"Generate new token (classic)"**
5. Donnez un nom : `eventsProject-push`
6. Cochez la permission : **`repo`** (toutes les sous-permissions)
7. Cliquez sur **"Generate token"**
8. **COPIEZ LE TOKEN** (vous ne le reverrez plus !)
9. Lors du `git push`, utilisez :
   - **Username** : votre username GitHub
   - **Password** : collez le token que vous venez de copier

### Option B : GitHub CLI

```powershell
# Installer GitHub CLI (si pas déjà installé)
# Puis :
gh auth login
```

## ✅ Vérification

Après le push, allez sur votre dépôt GitHub et vérifiez que vous voyez :
- ✅ Tous les fichiers du projet
- ✅ Le Jenkinsfile
- ✅ Le Dockerfile
- ✅ Les tests unitaires
- ✅ Les configurations

## 🆘 En cas de problème

Si vous avez déjà créé un dépôt avec un README :
```powershell
# Récupérer d'abord le README
git pull origin main --allow-unrelated-histories

# Puis pousser
git push -u origin main
```

Si le remote existe déjà :
```powershell
# Vérifier
git remote -v

# Si besoin, supprimer et recréer
git remote remove origin
git remote add origin https://github.com/VOTRE_USERNAME/eventsProject.git
```

