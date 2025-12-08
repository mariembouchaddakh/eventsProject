# Instructions pour pousser vers GitHub

## 📋 Étapes à suivre

### 1. Créer un nouveau dépôt sur GitHub

1. Allez sur https://github.com
2. Cliquez sur le bouton **"+"** en haut à droite → **"New repository"**
3. Nommez votre dépôt (ex: `eventsProject`)
4. **Ne cochez PAS** "Initialize this repository with a README"
5. Cliquez sur **"Create repository"**

### 2. Lier votre dépôt local à GitHub

Exécutez ces commandes dans votre terminal (remplacez `VOTRE_USERNAME` et `NOM_DU_REPO`):

```bash
# Ajouter le remote GitHub
git remote add origin https://github.com/VOTRE_USERNAME/NOM_DU_REPO.git

# Vérifier que le remote est bien ajouté
git remote -v

# Renommer la branche en main (si nécessaire)
git branch -M main

# Pousser le code vers GitHub
git push -u origin main
```

### 3. Exemple concret

Si votre username est `mariembouchaddakh` et votre repo `eventsProject`:

```bash
git remote add origin https://github.com/mariembouchaddakh/eventsProject.git
git branch -M main
git push -u origin main
```

## 🔐 Authentification

Si GitHub vous demande une authentification :

### Option 1 : Personal Access Token (recommandé)
1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Générer un nouveau token avec les permissions `repo`
3. Utiliser ce token comme mot de passe lors du `git push`

### Option 2 : GitHub CLI
```bash
gh auth login
```

### Option 3 : SSH (si vous avez configuré une clé SSH)
```bash
git remote set-url origin git@github.com:VOTRE_USERNAME/NOM_DU_REPO.git
git push -u origin main
```

## ✅ Vérification

Après le push, vérifiez sur GitHub que tous les fichiers sont bien présents :
- Jenkinsfile
- Dockerfile
- docker-compose.yml
- Tous les fichiers source
- Les tests unitaires
- Les configurations

## 🔄 Commandes utiles pour la suite

```bash
# Voir l'état des fichiers
git status

# Ajouter des modifications
git add .

# Créer un commit
git commit -m "Description des modifications"

# Pousser vers GitHub
git push

# Récupérer les dernières modifications
git pull
```

