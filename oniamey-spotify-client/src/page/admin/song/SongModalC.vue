<template>
  <a-modal
      :open="props.open"
      title="Form Song"
      @cancel="handleClose"
      @ok="handleCreateSong"
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

          <a-select
              v-else-if="field.component === 'a-select'"
              :max-tag-count="field.maxTagCount"
              :placeholder="field.placeholder"
              :show-search="field.showSearch"
              :filter-option="field.filterOption"
              :allow-clear="field.allowClear"
              :mode="field.mode"
              :options="field.options"
              v-model:value="modelRef[field.name]"
          ></a-select>

          <a-date-picker
              class="w-full"
              v-else-if="field.component === 'a-date-picker'"
              v-model:value="modelRef[field.name]"
              format="YYYY-MM-DD HH:mm"
              show-time
              :placeholder="field.placeholder"
          ></a-date-picker>

          <a-upload
              v-else-if="field.component === 'a-upload'"
              v-bind="field.customProps || {}"
              :max-count="1"
              v-model:value="modelRef[field.name]"
          >
            <a-button class="flex justify-between items-center gap-1">
              <upload-outlined></upload-outlined>
              Tải tệp âm thanh
            </a-button>
          </a-upload>

        </a-form-item>
      </template>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import {computed, createVNode, defineEmits, defineProps, reactive} from "vue";
import {Form, message, Modal, Upload} from "ant-design-vue";
import {ExclamationCircleOutlined, UploadOutlined} from "@ant-design/icons-vue";
import {toast} from "vue3-toastify";
import {useCreateSong, useGetGenres} from "@/infrastructure/services/service/admin/song.action.ts";
import {GenreResponse, SongRequest} from "@/infrastructure/services/api/admin/song.api.ts";
import {useUploadAudio} from "@/infrastructure/services/service/common/audio.action.ts";
import {AudioUploadRequest} from "@/infrastructure/services/api/common/audio.api.ts";
import {keepPreviousData} from "@tanstack/vue-query";
import dayjs from "dayjs";

const props = defineProps({
  open: Boolean,
});

const emit = defineEmits(["handleClose"]);

const {mutate: create} = useCreateSong();
const {mutate: uploadAudio} = useUploadAudio();

const {data: genres} = useGetGenres({
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});

const genreOptions = computed(() => genres?.value?.data || []);

const modelRef = reactive<SongRequest>({
  title: null,
  artist: null,
  releasedDate: null,
  genre: undefined,
  audioFile: null,
});

const validateFutureDate = (_: any, value: any) => {
  if (!value) return Promise.resolve();
  const currentDate = dayjs();
  const selectedDate = dayjs(value);
  if (selectedDate.isBefore(currentDate, 'minute')) {
    return Promise.reject(new Error('Ngày phát hành phải là một thời điểm trong tương lai (bao gồm cả giờ và phút)'));
  }
  return Promise.resolve();
};

const rulesRef = reactive({
  title: [{required: true, message: "Vui lòng nhập tên bài hát", trigger: "blur"}],
  artist: [{required: true, message: "Vui lòng nhập tên nghệ sĩ(người hát)", trigger: "blur"}],
  releasedDate: [
    {required: true, message: "Vui lòng nhập ngày phát hành", trigger: "blur"},
    {validator: validateFutureDate, trigger: 'blur'}
  ],
  genre: [{required: true, message: "Vui lòng nhập thể loại bài hát", trigger: "blur"}],
  audioFile: [{required: true, message: "Vui lòng thêm tệp âm thanh bài hát", trigger: "blur"}],
});

const {resetFields, validate, validateInfos} = Form.useForm(
    modelRef,
    rulesRef
);

const formFields = computed(() => [
  {
    label: "Tên bài hát",
    name: "title",
    component: "a-input",
    placeholder: "Nhâp tên bài hát"
  },
  {
    label: "Tên nghệ sĩ",
    name: "artist",
    component: "a-input",
    placeholder: "Nhâp tên nghệ sĩ"
  },
  {
    label: "Ngày phát hành",
    name: "releasedDate",
    component: "a-date-picker",
    placeholder: "Chọn thời gian phát hành"
  },
  {
    label: "Thể loại bài hát",
    name: "genre",
    component: "a-select",
    mode: "multiple",
    options: genreOptions.value?.map((genre: GenreResponse) => ({
      label: genre.name,
      value: genre.id,
      description: genre.description,
    })) || [],
    placeholder: "Chọn thể loại bài hát",
    showSearch: true,
    allowClear: true,
    filterOption: (input, option) => {
      return option.label.toLowerCase().includes(input.toLowerCase());
    },
    maxTagCount: 3,
  },
  {
    label: "Tệp âm thanh bài hát",
    name: "audioFile",
    component: "a-upload",
    customProps: {
      multiple: false,
      listType: "text",
      customRequest: (options: any) => {
        const {file, onSuccess, onError} = options;
        const audioUploadRequest: AudioUploadRequest = {
          file: file,
        };

        uploadAudio(audioUploadRequest, {
          onSuccess: (response) => {
            modelRef.audioFile = response.data.id;
            toast.success(response.message);
            if (onSuccess) onSuccess(response);
          },
          onError: (error) => {
            console.error(error);
            if (onError) onError(error);
          },
        });
      },
      beforeUpload: (file: any) => {
        const isAudio = file.type.startsWith("audio/");
        if (!isAudio) {
          message.error("Chỉ hỗ trợ upload file âm thanh!");
        }
        return isAudio || Upload.LIST_IGNORE;
      },
    },
  },
]);

const handleCreateSong = () => {
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