package com.lee.manageplatform.modules.sys.service.impl;

import com.lee.manageplatform.common.utils.StringUtil;
import com.lee.manageplatform.common.utils.multiwaytree.TreeNode;
import com.lee.manageplatform.common.utils.multiwaytree.TreeNodeUtil;
import com.lee.manageplatform.modules.sys.dao.SysDeptDao;
import com.lee.manageplatform.modules.sys.entity.SysDeptEntity;
import com.lee.manageplatform.modules.sys.entity.SysUserEntity;
import com.lee.manageplatform.modules.sys.service.SysDeptService;
import com.lee.manageplatform.modules.sys.service.SysUserService;
import com.qiniu.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("sysDeptService")
public class SysDeptServiceImpl implements SysDeptService {
    @Autowired
    private SysDeptDao sysDeptDao;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public SysDeptEntity queryObject(Long deptId) {
        return sysDeptDao.queryObject(deptId);
    }

    @Override
    public List<SysDeptEntity> queryList(Map<String, Object> map) {
        List<SysDeptEntity> list = sysDeptDao.queryList(map);
        for (SysDeptEntity sysDeptEntity:list) {
            Integer operator = StringUtil.isNotEmpty(sysDeptEntity.getUpdateId())?sysDeptEntity.getUpdateId():sysDeptEntity.getCreateId();
            sysDeptEntity.setOperator(sysUserService.queryObject(operator.longValue()).getUserName());
        }
        return list;
    }

    @Override
    public void save(SysDeptEntity sysDept) {
        sysDept.setCreateTime(new Date());
        sysDeptDao.save(sysDept);
    }

    @Override
    public void update(SysDeptEntity sysDept) {
        sysDept.setUpdateTime(new Date());
        sysDeptDao.update(sysDept);
    }

    @Override
    public void delete(Long deptId) {
        sysDeptDao.delete(deptId);
    }

    @Override
    public List<Long> queryDetpIdList(Long parentId) {
        return sysDeptDao.queryDetpIdList(parentId);
    }

    @Override
    public String getSubDeptIdList(Long deptId) {
        // 部门及子部门ID列表
        List<Long> deptIdList = new ArrayList<>();

        // 获取子部门ID
        List<Long> subIdList = queryDetpIdList(deptId);
        getDeptTreeList(subIdList, deptIdList);

        // 添加本部门
        deptIdList.add(deptId);

        String deptFilter = StringUtils.join(deptIdList, ",");
        return deptFilter;
    }

    /**
     * 递归
     */
    public void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList) {
        for (Long deptId : subIdList) {
            List<Long> list = queryDetpIdList(deptId);
            if (list.size() > 0) {
                getDeptTreeList(list, deptIdList);
            }

            deptIdList.add(deptId);
        }
    }

    /**
     * 封装一审部门的树形结构 zhoukai7
     *
     * @param deptId  部门ID
     * @param roleIds 角色ID
     * @return
     */
    @Override
    public Map<Long, String> encapsulationDeptTree(int deptId, Long roleIds) {
        Map<Long, String> maps = new HashMap<>();

        try {
            //调用生成多叉树方法
            TreeNode treeNode = this.spanningTree();
            //5、根据自己的deptId获取自己节点
            TreeNode treeNodeSelf = treeNode.findTreeNodeById(deptId);
            //6、根据自己的node节点获取父辈节点
            List<TreeNode> listTreeNodes = treeNodeSelf.getElders();

            //7、找到父辈中第一个带有mark标记的部门id
            TreeNode treeNodeMark = null;
            for (int i = 0; i < listTreeNodes.size(); i++) {
                TreeNode treeNodess = listTreeNodes.get(i);
                if (1 == treeNodess.getReviewsign() || "root".equals(treeNodess.getNodeName())) {
                    treeNodeMark = treeNodess;
                    break;
                }
            }
            //如果找不到直接返回null
            if (null == treeNodeMark ) {
                return null;
            }
            //8、找出该部门下的所有后辈部门id，
            List<TreeNode> lists = treeNodeMark.getJuniors();

            //直接去跟用户表关联查询然后判断该部门下所有的人那些有审核角色保存到Map

            for (int i = 0; i < lists.size(); i++) {
                TreeNode treeNodeRole = lists.get(i);
                List<SysUserEntity> listDept = sysUserService
                        .queryRoleById(Long.parseLong(String.valueOf(treeNodeRole.getOwnId())));
                maps.putAll(listDept.stream()
                        .filter(e -> roleIds.equals(e.getRoleId()))
                        .collect(Collectors.toMap(e -> e.getUserId(), v -> v.getUserName())));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return maps;

    }


    /**
     * 封装市场部门的树形结构 zhoukai7
     *
     *
     * @param roleIds 角色ID
     * @return
     */
    @Override
    public Map<Long, String> mktDeptTree(Long roleIds) {
        Map<Long, String> maps = new HashMap<>();

        try {
            //调用生成多叉树方法
            TreeNode treeNode = this.spanningTree();
            //6、根据自己的node节点获取子孙辈列表
            List<TreeNode> listTreeNodes = treeNode.getJuniors();


            //如果找不到直接返回null
            if (null == listTreeNodes || !(listTreeNodes.size() > 0) || listTreeNodes.isEmpty()) {
                return null;
            }
            //直接去跟用户表关联查询然后判断该部门下所有的人那些有审核角色保存到Map

            for (int i = 0; i < listTreeNodes.size(); i++) {
                TreeNode treeNodeRole = listTreeNodes.get(i);
                List<SysUserEntity> listDept = sysUserService
                        .queryRoleById(Long.parseLong(String.valueOf(treeNodeRole.getOwnId())));
                maps.putAll(listDept.stream()
                        .filter(e -> roleIds.equals(e.getRoleId()))
                        .collect(Collectors.toMap(e -> e.getUserId(), v -> v.getUserName())));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return maps;

    }


    /**
     * 生成多叉树结构
     * zhoukai7
     *
     * @return
     */
    private TreeNode spanningTree() {
        //1、首先获取所有部门信息
        Map<String, Object> map = new HashMap<String, Object>();
        List<SysDeptEntity> listAll = sysDeptDao.queryList(map);
        // new一个list保存全部节点
        List<TreeNode> listNode = new ArrayList<TreeNode>();

        //2、必须初始化一个root节点，保证根的唯一性
        TreeNode treeNode2 = new TreeNode();
        treeNode2.setParentId(0);
        treeNode2.setOwnId(0);
        treeNode2.setNodeName("root");
        listNode.add(treeNode2);

        //3、循环遍历所有的部门信息
        for (int j = 0; j < listAll.size(); j++) {

            SysDeptEntity sysDeptEntity = listAll.get(j);
            TreeNode treeNode = new TreeNode();
            treeNode.setParentId(Integer.parseInt(sysDeptEntity.getParentId().toString()));
            treeNode.setOwnId(Integer.parseInt(sysDeptEntity.getDeptId().toString()));
            treeNode.setNodeName(sysDeptEntity.getName());
            treeNode.setReviewsign(sysDeptEntity.getReviewsign());
            listNode.add(treeNode);
        }

        //4、绑定父节点跟子节点的关系
        TreeNodeUtil treeHelper = new TreeNodeUtil(listNode);
        TreeNode treeNode = treeHelper.getRoot();
        // 这里默认生成了一次多余的root子节点，必须执行删除
        treeNode.deleteChildNode(0);
        treeNode.traverseTreeAndSaveParentNode(treeNode);

        return treeNode;
    }


}
