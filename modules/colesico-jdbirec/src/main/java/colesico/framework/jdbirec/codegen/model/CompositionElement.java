/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.jdbirec.codegen.model;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.model.ClassElement;
import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.FieldElement;
import colesico.framework.jdbirec.Composition;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CompositionElement {

    private final RecordKitElement parentRecordKit;

    /**
     * Composition class type
     */
    private final ClassType originType;

    /**
     * Composition field in the parent composition
     */
    private final FieldElement originField;

    /**
     * Parent composition ref
     */
    private CompositionElement parentComposition;

    /**
     * Columns list imported from composition class
     *
     * @see Composition#columns()
     */
    private String[] importedColumns;

    /**
     * @see Composition#keyColumn()
     */
    private String keyColumn;

    private final Set<ColumnElement> columns = new LinkedHashSet<>();

    private final Set<CompositionElement> subCompositions = new LinkedHashSet<>();

    private String namePrefix = "";

    /**
     * Composition table name.
     * This is for joint records.
     */
    private String tableName;

    public CompositionElement(RecordKitElement parentRecordKit, ClassType originType, FieldElement originField) {
        this.parentRecordKit = parentRecordKit;
        this.originType = originType;
        this.originField = originField;
    }

    public boolean hasColumn(ColumnElement columnElement) {
        if (columns.contains(columnElement)) {
            return true;
        }
        for (CompositionElement sce : subCompositions) {
            if (sce.hasColumn(columnElement)) {
                return true;
            }
        }
        return false;
    }

    public void collectSubColumns(List<ColumnElement> allSubColumns) {
        allSubColumns.addAll(columns);
        for (CompositionElement comp : subCompositions) {
            comp.collectSubColumns(allSubColumns);
        }
    }

    public void addSubComposition(CompositionElement compElm) {
        compElm.setParentComposition(this);
        subCompositions.add(compElm);
    }

    public void addColumn(ColumnElement columnElm) {
        if (parentRecordKit.hasColumn(columnElm)) {
            throw CodegenException.of().message("Duplicate column: " + columnElm.getName()).element(columnElm.getOriginField()).build();
        }
        columns.add(columnElm);
        columnElm.setParentComposition(this);
    }

    public RecordKitElement getParentRecordKit() {
        return parentRecordKit;
    }

    public ClassType getOriginType() {
        return originType;
    }

    public FieldElement getOriginField() {
        return originField;
    }

    public CompositionElement getParentComposition() {
        return parentComposition;
    }

    public void setParentComposition(CompositionElement parentComposition) {
        this.parentComposition = parentComposition;
    }

    public String[] getImportedColumns() {
        return importedColumns;
    }

    public void setImportedColumns(String[] importedColumns) {
        this.importedColumns = importedColumns;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public Set<ColumnElement> getColumns() {
        return columns;
    }

    public Set<CompositionElement> getSubCompositions() {
        return subCompositions;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public String toString() {
        return "CompositionElement{" +
                "originClass=" + originType +
                ", originField=" + originField +
                '}';
    }
}
