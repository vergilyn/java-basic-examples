package com.vergilyn.examples.usage.u0004;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;

import org.apache.commons.io.IOUtils;

/**
 * @author vergilyn
 * @since 2021-05-17
 */
abstract class AbstractList2TreeTests {
    protected final List<TreeNode> _unmodifiableData;

    public AbstractList2TreeTests() {
        this._unmodifiableData = Collections.unmodifiableList(readData());
    }

    protected List<TreeNode> readData(){
        try(InputStream resource = this.getClass().getResourceAsStream("tree_node.json")) {
            String json = IOUtils.toString(resource, StandardCharsets.UTF_8);
            return JSON.parseArray(json, TreeNode.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
