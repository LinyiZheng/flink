/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.table.planner.operations.converters;

import org.apache.flink.sql.parser.ddl.SqlAlterCatalogReset;
import org.apache.flink.table.api.ValidationException;
import org.apache.flink.table.catalog.CommonCatalogOptions;
import org.apache.flink.table.operations.Operation;
import org.apache.flink.table.operations.ddl.AlterCatalogResetOperation;

import java.util.Set;

/** A converter for {@link SqlAlterCatalogReset}. */
public class SqlAlterCatalogResetConverter implements SqlNodeConverter<SqlAlterCatalogReset> {

    @Override
    public Operation convertSqlNode(
            SqlAlterCatalogReset sqlAlterCatalogReset, ConvertContext context) {
        String type = CommonCatalogOptions.CATALOG_TYPE.key();
        Set<String> resetKeys = sqlAlterCatalogReset.getResetKeys();
        if (resetKeys.isEmpty() || resetKeys.contains(type)) {
            String exMsg =
                    resetKeys.isEmpty()
                            ? "ALTER CATALOG RESET does not support empty key"
                            : String.format(
                                    "ALTER CATALOG RESET does not support changing '%s'", type);
            throw new ValidationException(exMsg);
        }
        return new AlterCatalogResetOperation(
                sqlAlterCatalogReset.catalogName(), sqlAlterCatalogReset.getResetKeys());
    }
}
