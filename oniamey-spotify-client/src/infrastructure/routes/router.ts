import {createRouter, createWebHistory, RouteRecordRaw} from "vue-router";
import {ROUTES_CONSTANTS} from "@/infrastructure/constants/path.ts";
import {ROLES} from "@/infrastructure/constants/role.ts";
import {useAuthStore} from "@/infrastructure/stores/auth.ts";

const routes: Array<RouteRecordRaw> = [
    {
        path: ROUTES_CONSTANTS.AUTHENTICATION.path,
        name: ROUTES_CONSTANTS.AUTHENTICATION.name,
        component: () => import("@/layout/authentication/Spotify.vue"),
        redirect: ROUTES_CONSTANTS.AUTHENTICATION.children.LOGIN,
        meta: {
            requiresAuth: false
        },
        children: [
            {
                path: ROUTES_CONSTANTS.AUTHENTICATION.children.LOGIN.path,
                name: ROUTES_CONSTANTS.AUTHENTICATION.children.LOGIN.name,
                component: () => import('@/layout/authentication/Login.vue'),
                meta: {
                    requiresAuth: false
                },
            },
            {
                path: ROUTES_CONSTANTS.AUTHENTICATION.children.REGISTER.path,
                name: ROUTES_CONSTANTS.AUTHENTICATION.children.REGISTER.name,
                component: () => import('@/layout/authentication/Register.vue'),
                meta: {
                    requiresAuth: false
                },
            },
        ],
    },
    {
        path: ROUTES_CONSTANTS.USER.path,
        name: ROUTES_CONSTANTS.USER.name,
        component: () => import("@/layout/user/User.vue"),
        meta: {
            requiresRole: ROLES.USER,
            requiresAuth: true
        },
        redirect: ROUTES_CONSTANTS.USER.children.LISTENING_TO_MUSIC,
        children: [
            {
                path: ROUTES_CONSTANTS.USER.children.LISTENING_TO_MUSIC.path,
                name: ROUTES_CONSTANTS.USER.children.LISTENING_TO_MUSIC.name,
                component: () => import('@/page/user/Feature.vue'),
                meta: {
                    requiresRole: ROLES.USER,
                    requiresAuth: true
                },
            },
        ],
    },
    {
        path: ROUTES_CONSTANTS.ADMIN.path,
        name: ROUTES_CONSTANTS.ADMIN.name,
        component: () => import("@/layout/admin/Admin.vue"),
        meta: {
            requiresRole: ROLES.ADMIN,
            requiresAuth: true
        },
        redirect: ROUTES_CONSTANTS.ADMIN.children.USER,
        children: [
            {
                path: ROUTES_CONSTANTS.ADMIN.children.USER.path,
                name: ROUTES_CONSTANTS.ADMIN.children.USER.name,
                component: () => import('@/page/admin/user/User.vue'),
                meta: {
                    requiresRole: ROLES.ADMIN,
                    requiresAuth: true
                },
            },
            {
                path: ROUTES_CONSTANTS.ADMIN.children.SONG.path,
                name: ROUTES_CONSTANTS.ADMIN.children.SONG.name,
                component: () => import('@/page/admin/song/Song.vue'),
                meta: {
                    requiresRole: ROLES.ADMIN,
                    requiresAuth: true
                },
            },
            {
                path: ROUTES_CONSTANTS.ADMIN.children.LISTENING_TO_MUSIC.path,
                name: ROUTES_CONSTANTS.ADMIN.children.LISTENING_TO_MUSIC.name,
                component: () => import('@/page/admin/ListeningToMusic.vue'),
                meta: {
                    requiresRole: ROLES.ADMIN,
                    requiresAuth: true
                },
            },
        ],
    },
    {
        path: ROUTES_CONSTANTS.REDIRECT.path,
        name: ROUTES_CONSTANTS.REDIRECT.name,
        component: () => import("@/infrastructure/routes/guard/Redirect.vue"),
        meta: {requiresAuth: false},
    },
    {
        path: ROUTES_CONSTANTS.NOT_FOUND.path,
        name: ROUTES_CONSTANTS.NOT_FOUND.name,
        component: () => import("@/page/exception/404/NotFound.vue"),
        meta: {requiresAuth: false},
    },
];

const route = createRouter({
    history: createWebHistory(),
    routes,
});

route.beforeEach((to, from, next) => {
    const authStore = useAuthStore();
    const requiresAuth = to.matched && Array.isArray(to.matched) && to.matched.some((record) => record?.meta?.requiresAuth);
    const requiresRole = to.matched && Array.isArray(to.matched) && to.matched.some((record) => record?.meta?.requiresRole);
    const userRole = authStore?.user?.roleCode;
    if (userRole === null && requiresAuth) {
        next({name: ROUTES_CONSTANTS.AUTHENTICATION.children.LOGIN.name});
    } else if (requiresAuth && !authStore.isAuthenticated) {
        next({name: ROUTES_CONSTANTS.AUTHENTICATION.children.LOGIN.name});
    } else if (requiresRole && (!userRole || userRole !== to.meta.requiresRole)) {
        next({name: ROUTES_CONSTANTS.AUTHENTICATION.children.LOGIN.name});
    } else {
        next();
    }
});

export default route;
