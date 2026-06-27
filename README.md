# Jenki ns CI/CD with ArgoCD, Vault, SonarQube and Trivy

> **Stage 11 of 12 — Career Progression Project**  
> Portfolio project by **Yugandhar Ethamukkala**.

Production-like DevSecOps pipeline project using Jenkins, Maven, SonarQube, Docker, Trivy, Vault-based secrets, artifact publishing, S3 report upload, and ArgoCD deployment workflow.

## Career Progression Story

Enterprise DevSecOps step: I combined Jenkins, security scanning, secret management, artifact handling, and GitOps deployment.

This repo is part of my 12-project DevOps portfolio path. The goal is to show steady growth from CI/CD foundations into AWS cloud, Kubernetes, GitOps, observability, DevSecOps, progressive delivery, and AI-enabled deployments.

## What This Project Demonstrates

- Strong enterprise CI/CD project with security and secret-management story.
- Good for explaining Jenkins credentials, Vault integration, image scanning, and GitOps deployment.
- Includes existing screenshots that make the repository visually stronger.

## Tech Stack

`Java` `Maven` `Jenkins` `Docker` `Kubernetes` `ArgoCD` `Vault` `SonarQube` `Trivy` `AWS S3`

## Architecture

```mermaid
flowchart LR
  Dev[Developer Commit] --> Jenkins[Jenkins Pipeline]
  Jenkins --> Maven[Maven Build]
  Maven --> Sonar[SonarQube Quality Gate]
  Maven --> Docker[Docker Image]
  Docker --> Trivy[Trivy Scan]
  Jenkins --> Vault[Vault Secrets]
  Docker --> Registry[Image Registry]
  Registry --> ArgoCD[ArgoCD]
  ArgoCD --> K8S[Kubernetes]
```

## Repository Structure

```text
.
├── Dockerfile
├── Jenkinsfile
├── LICENSE
├── README.md
├── REPO_UPLOAD_CHECKLIST.md
├── docs/
├── images/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── project.yaml
├── src/
```

## Prerequisites

- Git
- Docker where containers are used
- Cloud CLI/tools only when deploying cloud resources
- `kubectl`, `kind`, `terraform`, `sam`, `maven`, `npm`, or `python` depending on the project
- Never commit real `.env` files, API keys, access keys, kubeconfigs, private keys, or tokens

## Local Run

```bash
mvn clean package
docker build -t jenkins-argocd-vault-app:local .
docker run --rm -p 8080:8080 jenkins-argocd-vault-app:local
```

## Validation Before GitHub Upload

Run these checks before pushing major changes:

```bash
mvn -q test
test -f Jenkinsfile
docker build -t jenkins-argocd-vault-app:local .
```

## Deployment Overview

1. Create Jenkins credentials for Docker registry, Vault, Kubernetes, and AWS/S3 if used.
2. Run Jenkins pipeline stages for build, test, scan, image push, and manifest update.
3. Use ArgoCD to sync the application into Kubernetes.
4. Review SonarQube and Trivy results before promotion.

## Screenshots

The project already included these snapshots, so I added them into the README. Replace them with your own latest run screenshots when you execute the lab.

### Architecture diagram

![Architecture diagram](docs/screenshots/architecture.png)

### Jenkins dashboard

![Jenkins dashboard](docs/screenshots/jenkins-dashboard.png)

### Pipeline view

![Pipeline view](docs/screenshots/jenkins-pipeline.png)

### ArgoCD sync view

![ArgoCD sync view](docs/screenshots/argocd-sync.png)

### Trivy scan report

![Trivy scan report](docs/screenshots/trivy-report.png)

### SonarQube quality gate

![SonarQube quality gate](docs/screenshots/sonarqube-quality-gate.png)

## Cleanup / Cost Control

Run cleanup commands after testing so cloud resources do not keep charging:

```bash
kubectl delete -f manifests/ --ignore-not-found=true || true
docker image rm jenkins-argocd-vault-app:local || true
aws s3 rm s3://YOUR-SCAN-REPORT-BUCKET/ --recursive || true
```

## Security Notes

- Use GitHub Actions OIDC, Jenkins credentials, AWS Secrets Manager, Vault, or Kubernetes Secrets instead of hard-coded keys.
- Keep `.env` files local and commit only `.env.example` with safe placeholders.
- Review Terraform plans before apply/destroy.
- Do not publish account IDs, private IPs, public IPs from your lab, billing pages, or credential screenshots.

## How I Would Explain This in an Interview

I built this project as part of my DevOps portfolio to show hands-on experience with the tools used in real delivery environments. The focus is not only on writing code, but also on creating a repeatable workflow for build, validation, deployment, security, monitoring, and cleanup.

In a real project, I would connect this type of setup with environment-specific variables, approval gates, secrets management, monitoring dashboards, and rollback steps so teams can release safely and troubleshoot faster.

---

<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0:0F2027,50:2C5364,100:00C9FF&height=120&section=footer&text=Let's%20Connect&fontColor=ffffff&fontSize=32&fontAlignY=70" />
</p>

<h2 align="center">🤝 Connect With Me</h2>

<p align="center">
  <em>
    Thanks for visiting this project! I’m continuously building hands-on DevOps, Cloud, Automation, and AI-enabled engineering projects to improve real-world deployment, monitoring, and infrastructure skills.
  </em>
</p>

<p align="center">
  <img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&size=22&duration=2500&pause=800&color=00C9FF&center=true&vCenter=true&width=650&lines=DevOps+%7C+Cloud+%7C+Automation;CI%2FCD+%7C+Docker+%7C+Kubernetes+%7C+Terraform;Building+real-world+projects+one+commit+at+a+time" alt="Typing SVG" />
</p>

<p align="center">
  <a href="https://github.com/yugandhar99" target="_blank" rel="noopener noreferrer">
    <img src="https://img.shields.io/badge/GitHub-Follow-181717?style=flat&logo=github&logoColor=white" alt="GitHub" />
  </a>
  <a href="https://www.linkedin.com/in/yugandhar-devops" target="_blank" rel="noopener noreferrer">
    <img src="https://img.shields.io/badge/LinkedIn-Connect-0A66C2?style=flat&logo=linkedin&logoColor=white" alt="LinkedIn" />
  </a>
  <a href="https://yugandhar-portfolio-psi.vercel.app/" target="_blank" rel="noopener noreferrer">
    <img src="https://img.shields.io/badge/Portfolio-View%20My%20Work-FF5722?style=flat&logo=vercel&logoColor=white" alt="Portfolio" />
  </a>
  <a href="mailto:yugandharethamukkala1999@gmail.com">
    <img src="https://img.shields.io/badge/Email-Contact%20Me-D14836?style=flat&logo=gmail&logoColor=white" alt="Email" />
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Focus-DevOps%20Engineering-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/Cloud-AWS%20%7C%20Azure%20%7C%20GCP-orange?style=flat-square" />
  <img src="https://img.shields.io/badge/IaC-Terraform-purple?style=flat-square" />
  <img src="https://img.shields.io/badge/Containers-Docker%20%7C%20Kubernetes-2496ED?style=flat-square" />
</p>

---

<p align="center">
  ⭐ If this project added value, feel free to star the repository and connect with me!
</p>

<p align="center">
  <strong>Built with ❤️ using modern DevOps practices</strong>
</p>

