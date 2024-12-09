<template>
  <a-layout>
    <a-layout-sider
        theme="light"
        class="w[300px]"
        v-model:collapsed="collapsed"
        :trigger="null"
        collapsible
    >
      <div class="w-full flex items-center justify-center">
        <img class="w-full" :src="logo" alt="logo"/>
      </div>
      <a-menu
          v-model:openKeys="state.openKeys"
          v-model:selectedKeys="state.selectedKeys"
          mode="inline"
          theme="light"
          :inline-collapsed="collapsed"
          :items="items"
          @click="handleClick"
      ></a-menu>
    </a-layout-sider>

    <a-layout>
      <a-layout-header class="pl-3 mt-1" style="background-color: white">
        <div class="user-info flex items-center justify-between">
          <div class="cursor-pointer" @click="collapsed = !collapsed">
            <component
                :is="collapsed ? MenuUnfoldOutlined : MenuFoldOutlined"
                class="text-xl"
            />
          </div>
          <a-dropdown placement="bottomRight" arrow>
            <div class="flex items-center cursor-pointer">
              <a-avatar
                  v-if="userInfo?.profilePicture"
                  :src="userInfo?.profilePicture"
                  size="large"
              >
                {{ userInfo?.userName }}
              </a-avatar>
              <span class="ml-2 truncate">
                {{ userInfo?.userName }}
              </span>
            </div>
            <template #overlay>
              <a-menu>
                <a-menu-item key="logout" @click="handleLogout">
                  Đăng xuất
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </a-layout-header>
      <a-layout-content class="mx-4">
        <div class="min-h-[calc(100vh-9.5rem)] bg-white">
          <router-view/>
        </div>
      </a-layout-content>
      <a-layout-footer class="text-center">
        Spotify Oniamey ©2024 Created by Oniamey
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script lang="ts" setup>
import logo from "@/assets/image/logo/nobg/svg/logo-big-1.svg"
import {computed, h, reactive, ref, watch} from 'vue';
import {useRouter} from 'vue-router';
import {ROUTES_CONSTANTS} from "@/infrastructure/constants/path.ts";
import {useAuthStore} from "@/infrastructure/stores/auth.ts";
import {
  DeploymentUnitOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UsergroupAddOutlined,
  WeiboOutlined
} from '@ant-design/icons-vue';
import {MenuProps} from "ant-design-vue";

const state = reactive({
  selectedKeys: ['1'],
  openKeys: ['1'],
  preOpenKeys: ['1'],
});

const items = reactive([
  {
    key: '1',
    icon: () => h(UsergroupAddOutlined),
    label: 'User',
    title: 'User',
  },
  {
    key: '2',
    icon: () => h(WeiboOutlined),
    label: 'Song',
    title: 'Song',
  },
  {
    key: '3',
    icon: () => h(DeploymentUnitOutlined),
    label: 'Listening to song',
    title: 'Listening to song',
  },
]);

watch(
    () => state.openKeys,
    (_val, oldVal) => {
      state.preOpenKeys = oldVal;
    },
);

const auth = useAuthStore();
const userInfo = computed(() => auth.user);
const collapsed = ref<boolean>(false);
const router = useRouter();

const handleLogout = () => {
  auth.logout();
  router.push(ROUTES_CONSTANTS.AUTHENTICATION.path);
};

const menuItems = ref([
  {
    key: "1",
    label: "UserManagement",
    path: ROUTES_CONSTANTS.ADMIN.children.USER.path,
  },
  {
    key: "2",
    label: "SongManagement",
    path: ROUTES_CONSTANTS.ADMIN.children.SONG.path,
  },
  {
    key: "3",
    label: "Listening to song",
    path: ROUTES_CONSTANTS.ADMIN.children.LISTENING_TO_MUSIC.path,
  },
]);

const handleClick: MenuProps['onClick'] = e => {
  const key = e?.key;
  const item = menuItems.value.find(
      (menuItem) => menuItem.key === key ||
          menuItem.children?.some((child) => child.key === key)
  )
  if (item) {
    const target = item.children?.find((child) => child.key === key) || item;
    if (target?.path) {
      router.push(target.path);
    }
  }
};
</script>

<script lang="ts">
export default {
  name: "AdminPage",
};
</script>
