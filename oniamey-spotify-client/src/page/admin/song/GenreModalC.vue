<template>
  <a-modal
      :open="props.open"
      title="Form Genre"
      @cancel="handleClose"
      @ok="handleCreateGenre"
      ok-text="Thêm"
      cancel-text="Hủy"
      destroyOnClose
      centered
  >
    <a-form layout="vertical" class="pt-3">
      <template v-for="field in formFields">
        <a-form-item
            :label="field.label"
            :name="field.name"
            v-bind="validateInfos[field.name]"
        >
          <a-input
              v-if="field.component === 'a-input'"
              v-model:value="modelRef[field.name]"
          ></a-input>
        </a-form-item>
      </template>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import {computed, createVNode, defineEmits, defineProps, reactive} from "vue";
import {Form, Modal} from "ant-design-vue";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import {toast} from "vue3-toastify";
import {GenreRequest} from "@/infrastructure/services/api/admin/song.api.ts";
import {useCreateGenre} from "@/infrastructure/services/service/admin/song.action.ts";

const props = defineProps({
  open: Boolean,
});

const emit = defineEmits(["handleClose"]);

const {mutate: create} = useCreateGenre();

const modelRef = reactive<GenreRequest>({
  genre: undefined,
});

const rulesRef = reactive({
  genre: [{required: true, message: "Vui lòng nhập thể loại bài hát", trigger: "blur"}],
});

const {resetFields, validate, validateInfos} = Form.useForm(
    modelRef,
    rulesRef
);

const formFields = computed(() => [
  {
    label: "Tên thể loại",
    name: "genre",
    component: "a-input",
    placeholder: "Nhâp tên thể loại"
  },
]);

const handleCreateGenre = () => {
  Modal.confirm({
    content: "Bạn chắc chắn muốn thêm?",
    icon: createVNode(ExclamationCircleOutlined),
    centered: true,
    async onOk() {
      try {
        await validate();
        create(modelRef, {
          onSuccess: (result) => {
            toast.success(result?.message);
            handleClose();
          },
          onError: (error: any) => {
            toast.error(
                error?.response?.data?.message
            );
          },
        });
      } catch (error: any) {
        console.error("🚀 ~ handleCreate ~ error:", error);
        if (error?.response) {
          toast.warning(
              error?.response?.data?.message
          );
        } else if (error?.errorFields) {
          toast.warning("Vui lòng nhập đầy đủ các trường dữ liệu");
        }
      }
    },
    cancelText: "Huỷ",
    onCancel() {
      Modal.destroyAll();
      resetFields();
    },
  });
};

const handleClose = () => {
  emit("handleClose");
  resetFields();
};
</script>