<template>
    <div>
        <div>
            <div style="display: flex;justify-content: space-between">
                <div>
                    <el-input placeholder="请输入员工id进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                              clearable
                              @clear="initEmpEcs"
                              style="width: 350px;margin-right: 10px" v-model="keyword"
                              @keydown.enter.native="initEmpEcs" :disabled="showAdvanceSearchView"></el-input>
                    <el-button icon="el-icon-search" type="primary" @click="initEmpEcs" :disabled="showAdvanceSearchView">
                        搜索
                    </el-button>
                    <el-button type="primary" @click="showAdvanceSearchView = !showAdvanceSearchView">
                        <i :class="showAdvanceSearchView?'fa fa-angle-double-up':'fa fa-angle-double-down'"
                           aria-hidden="true"></i>
                        高级搜索
                    </el-button>
                </div>
                <div>
                    <el-button type="primary" icon="el-icon-plus" @click="showAddEmpEcView">
                        添加奖罚记录
                    </el-button>
                </div>
            </div>
            <transition name="slide-fade">
                <div v-show="showAdvanceSearchView"
                     style="border: 1px solid #409eff;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px;">
                    <el-row>
                        <el-col :span="5">
                            奖罚分:
                            <el-input placeholder="请输入奖罚分进行搜索" v-model="searchValue.ecPoint"></el-input>
                        </el-col>
                        <el-col :span="4">
                            奖罚原因:
                            <el-input placeholder="请输入奖罚原因进行搜索" v-model="searchValue.ecReason"></el-input>
                        </el-col>
                        <el-col :span="7">
                            奖罚类别:
                            <el-radio-group v-model="searchValue.ecType">
                                <el-radio label="劳动合同">奖</el-radio>
                                <el-radio label="劳务合同">罚</el-radio>
                            </el-radio-group>
                        </el-col>
                    </el-row>
                    <el-row style="margin-top: 10px">
                        <el-col :span="10">
                            奖罚日期:
                            <el-date-picker
                                    v-model="searchValue.beginDateScope"
                                    type="daterange"
                                    size="mini"
                                    unlink-panels
                                    value-format="yyyy-MM-dd"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                            </el-date-picker>
                        </el-col>
                        <el-col :span="5" :offset="4">
                            <el-button size="mini">取消</el-button>
                            <el-button size="mini" icon="el-icon-search" type="primary" @click="initEmpEcs('advanced')">搜索</el-button>
                        </el-col>
                    </el-row>
                </div>
            </transition>
        </div>
        <div style="margin-top: 10px">
            <el-table
                    :data="empEcs"
                    stripe
                    border
                    v-loading="loading"
                    element-loading-text="正在加载..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="eid"
                        fixed
                        align="left"
                        label="员工编号"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="ecDate"
                        label="奖罚日期"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="ecReason"
                        label="奖罚原因"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="ecPoint"
                        width="95"
                        align="left"
                        label="奖罚分">
                </el-table-column>
                <el-table-column
                        prop="ecType"
                        width="150"
                        align="left"
                        label="奖罚类型">
                </el-table-column>
                <el-table-column
                        prop="remark"
                        width="70"
                        label="备注">
                </el-table-column>
                <el-table-column
                        fixed="right"
                        width="200"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button @click="showEditEmpEcView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
                        <el-button @click="deleteEmpEc(scope.row)" style="padding: 3px" size="mini" type="danger">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div style="display: flex;justify-content: flex-end">
                <el-pagination
                        background
                        @current-change="currentChange"
                        @size-change="sizeChange"
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total">
                </el-pagination>
            </div>
        </div>
        <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                width="80%">
            <div>
                <el-form :model="empEc" :rules="rules" ref="empEcForm">
                    <el-row>
                        <el-col :span='6'>
                            <el-select v-model="empEc.eid" placeholder="姓名" size="mini"
                                       style="width: 130px;">
                                <el-option
                                        v-for="item in empNames"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="奖罚原因:" prop="ecReason">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="empEc.ecReason"
                                          placeholder="请输入奖罚原因"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="奖罚类别:" prop="ecType">
                                <el-radio-group v-model="empEc.ecType">
                                    <el-radio label="0">奖</el-radio>
                                    <el-radio label="1">罚</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="奖罚分:" prop="ecPoint">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="empEc.ecPoint"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="备注:" prop="remark">
                                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                                          v-model="empEc.remark" placeholder="请输入备注"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="奖罚日期:" prop="ecDate">
                                <el-date-picker
                                        v-model="empEc.ecDate"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 130px;"
                                        placeholder="奖罚日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doAddEmpEc">确 定</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "PerEc",
        data() {
            return {
                searchValue: {
                    ecPoint: null,
                    ecType: null,
                    ecReason: null,
                    beginDateScope: null
                },
                title: '',
                showAdvanceSearchView: false,
                page: 1,
                size: 10,
                total: 0,
                keyword: '',
                loading: false, 
                empEcs: [],
                dialogVisible: false,
                empEc: {
                    id: 1,
                    eid: 1,
                    ecDate: "2023-06-03",
                    ecReason: "ecReason",
                    ecPoint: 2,
                    ecType: 1,
                    remark: "remark"
                },
                empNames: [],
                rules: {
                    ecDate: [{required: true, message: '请输入奖罚日期', trigger: 'blur'}],
                    ecReason: [{required: true, message: '请输入奖罚原因', trigger: 'blur'}],
                    ecPoint: [{required: true, message: '请输入奖罚分', trigger: 'blur'}],
                    ecType: [{required: true, message: '请输入奖罚类型', trigger: 'blur'}],
                }
            }
        },
        mounted() {
            this.initEmpEcs();
            this.initEmployeeNames();
        },
        methods: {
            onSuccess(response, file, fileList) {
                this.initEmpEcs();
            },
            sizeChange(currentSize) {
                this.size = currentSize;
                this.initEmpEcs();
            },
            currentChange(currentPage) {
                this.page = currentPage;
                this.initEmpEcs('advanced');
            },
            showEditEmpEcView(data) {
                this.title = '编辑奖罚信息';
                this.empEc = data;
                this.dialogVisible = true;
            },
            deleteEmpEc(data) {
                this.$confirm('此操作将永久删除编号为【' + data.id + '】的奖罚记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/employee/empEc/" + data.id).then(resp => {
                        if (resp) {
                            this.initEmpEcs();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            emptyEmpEc() {
                this.empEc ={
                    id: null,
                    eid: null,
                    ecDate: "",
                    ecReason: "",
                    ecPoint: null,
                    ecType: null,
                    remark: ""
                }
            },
            doAddEmpEc() {
                if (this.empEc.id) {
                    this.$refs['empEcForm'].validate(valid => {
                        if (valid) {
                            this.putRequest("/employee/empEc/", this.empEc).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmpEcs();
                                }
                            })
                        }
                    });
                } else {
                    this.$refs['empEcForm'].validate(valid => {
                        if (valid) {
                            this.postRequest("/employee/empEc/", this.empEc).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmpEcs();
                                }
                            })
                        }
                    });
                }
            },
            showAddEmpEcView() {
                this.emptyEmpEc();
                this.title = '添加奖罚记录';
                this.dialogVisible = true;
            },
            initEmpEcs(type) {
                this.loading = true;
                let url = '/employee/empEc/?page=' + this.page + '&size=' + this.size;
                if (type && type == 'advanced') {
                    if (this.searchValue.ecPoint) {
                        url += '&ecPoint=' + this.searchValue.ecPoint;
                    }
                    if (this.searchValue.ecReason) {
                        url += '&ecReason=' + this.searchValue.ecReason;
                    }
                    if (this.searchValue.ecType) {
                        url += '&ecType=' + this.searchValue.ecType;
                    }
                    if (this.searchValue.beginDateScope) {
                        url += '&beginDateScope=' + this.searchValue.beginDateScope;
                    }
                } else {
                    url += '&eid=' + this.keyword;
                }
                this.getRequest(url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.empEcs = resp.data;
                        this.total = resp.total;
                    }
                });
            },
            initEmployeeNames() {
                let url = '/employee/empEc/getAllEmployeeNames';
                this.getRequest(url).then(resp => {
                    if(resp) {
                        this.empNames = resp;
                    }
                })
            }
        }
    }
</script>

<style>
    /* 可以设置不同的进入和离开动画 */
    /* 设置持续时间和动画函数 */
    .slide-fade-enter-active {
        transition: all .8s ease;
    }

    .slide-fade-leave-active {
        transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
    }

    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */
    {
        transform: translateX(10px);
        opacity: 0;
    }
</style>