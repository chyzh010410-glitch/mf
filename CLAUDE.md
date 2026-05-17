# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Workspace Overview

苗丰施肥管控平台 (MiaoFeng Fertilizer Management Platform) — a full-stack application for tree fertilization management.

- `mf-fertilizer/` — Java 17 Spring Boot 3.2.5 backend (mature)
- `mf-frontend/` — Frontend SPA (to be built)

## Backend: mf-fertilizer

### Build & Run

```bash
# Build all modules
cd mf-fertilizer && mvn clean package -DskipTests

# Run (the api module produces the executable jar)
java -jar fertilizer-api/target/fertilizer-api-1.0.0.jar

# The app starts on port 8080 with dev profile active
# Swagger UI: http://localhost:8080/doc.html
```

### Module Architecture

Three-tier Maven multi-module: `fertilizer-api` → `fertilizer-core` → `fertilizer-common`

| Module | Purpose |
|--------|---------|
| `fertilizer-common` | Shared models: entities (`BaseEntity` with雪花ID), DTOs, VOs, constants, exceptions, utils (`JwtUtil`, `SeasonUtil`) |
| `fertilizer-core` | Business logic: MyBatis-Plus mappers (extend `BaseMapper`, no XML), service interfaces (extend `IService`), service implementations |
| `fertilizer-api` | Web layer: `FertilizerApplication` entry point, config classes (CORS, JWT interceptor, MyBatis-Plus, Redis, Swagger), 5 REST controllers |

### Key Conventions

- **Response format**: All endpoints return `ResultVO<T>` with `code`, `msg`, `data`
- **Pagination**: List endpoints accept `PageDTO` (page/size), return `PageVO<T>` (total/page/size/records)
- **Validation**: DTOs use Jakarta Validation annotations (`@NotBlank`, `@NotNull`), controllers use `@Valid`
- **DI**: `@RequiredArgsConstructor` on all classes, no `@Autowired`
- **Entities**: All extend `BaseEntity` —雪花ID primary key, `createTime`/`updateTime` auto-filled, `deleted` logical delete
- **Cache**: Redis for JWT tokens (`login:token:<token>`), tree species list, fertilizer list, recommendation results (24h TTL)
- **Auth**: JWT (HMAC-SHA, 7-day expiry) + Redis token store. Interceptor checks Redis key existence; excludes `/login`, Swagger paths, `/error`
- **DB**: 5 tables — `sys_user`, `tree`, `fertilizer`, `fertilization_record`, `fertilization_rule`. No foreign keys; indexing on query fields; `utf8mb4`/InnoDB

### Tech Stack (fixed, do not substitute)

Java 17, Spring Boot 3.2.5, MyBatis-Plus 3.5.9, MySQL 8.0+, Redis, JWT (jjwt 0.12.6), Knife4j 4.3.0, Hutool 5.8.28, Lombok

### API Endpoints Summary

| Prefix | Controller | Key Operations |
|--------|-----------|----------------|
| `/login`, `/logout` | `LoginController` | Login returns JWT token; logout invalidates |
| `/tree` | `TreeController` | CRUD + paginated search (species/status/age filters) + `/tree/species` cached list |
| `/fertilizer` | `FertilizerController` | CRUD + paginated search (name/type) + `/fertilizer/list` cached list |
| `/record` | `FertilizationRecordController` | CRUD + paginated search (tree/fertilizer/date range) + `/record/stats` aggregation |
| `/rule` | `FertilizationRuleController` | CRUD + `/rule/recommend` — matches rules by species, age range, season, returns ranked fertilizer recommendations |

### Database

- Host: `localhost:3306`, database: `fertilizer`, credentials: `root/123456`
- Redis: `localhost:6379`, password: `123456`
- Schema init script: `fertilizer-api/src/main/resources/db/schema.sql` (includes sample data: 2 users, 4 trees, 5 fertilizers, 8 rules)
- Default admin: `admin/admin123`

### Project Skill File

Full specification at `.claude/skills/mf-fertilizer.md` — read it when making significant backend changes.

## Frontend: mf-frontend

### Tech Stack (fixed, do not substitute)

Vue 3 (Composition API) + Vite + JavaScript + Element Plus + Axios + Vue Router + Pinia

### Commands

```bash
cd mf-frontend && npm install && npm run dev
# Dev server at http://localhost:5173
```

### Directory Structure (enterprise standard)

```
mf-frontend/
├── public/
├── src/
│   ├── api/          # Axios API modules per business domain
│   ├── assets/       # Static assets
│   ├── components/   # Shared components
│   ├── router/       # Vue Router config
│   ├── store/        # Pinia stores (auth)
│   ├── styles/       # Global styles + CSS variables
│   ├── utils/        # Axios wrapper, request interceptors
│   └── views/        # Page components grouped by feature
├── index.html
├── vite.config.js
└── package.json
```

### Key Conventions

- **Theme**: White + green (`#2d8c4a` primary, `#f0f9f4` background)
- **Layout**: Sidebar + top nav + main content (classic admin shell)
- **HTTP**: `src/utils/request.js` wraps Axios — baseURL `http://localhost:8080`, auto-attaches `Authorization: Bearer <token>`, handles 401 redirect to `/login`
- **Auth flow**: Login → store token in Pinia + localStorage → interceptor reads from store → logout clears both
- **API modules**: One file per backend controller in `src/api/` (auth.js, tree.js, fertilizer.js, record.js, rule.js)
- **Pages**: All pages go through the Layout shell. `/login` is standalone.
- **Routing**: `router.beforeEach` checks token for protected routes, redirects to `/login` if absent

### Pages

| Route | Component | Description |
|-------|-----------|-------------|
| `/login` | `views/login/index.vue` | Login with water-drop → seedling animation |
| `/` | `views/layout/index.vue` | Shell: sidebar menu + top bar + `<router-view>` |
| `/fertilizer` | `views/fertilizer/index.vue` | Fertilizer CRUD list (search + table + dialog) |
| `/tree` | `views/tree/index.vue` | Tree management (placeholder) |
| `/record` | `views/record/index.vue` | Fertilization records (placeholder) |
| `/rule` | `views/rule/index.vue` | Fertilization rules (placeholder) |
