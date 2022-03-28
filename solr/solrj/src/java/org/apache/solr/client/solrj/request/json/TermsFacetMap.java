/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.client.solrj.request.json;

import java.util.Map;

/**
 * Represents a "terms" facet in a JSON query request.
 *
 * <p>Ready for use in {@link JsonQueryRequest#withFacet(String, Map)}
 */
public class TermsFacetMap extends JsonFacetMap<TermsFacetMap> {
  public TermsFacetMap(String fieldName) {
    super("terms");

    put("field", fieldName);
  }

  @Override
  public TermsFacetMap getThis() {
    return this;
  }

  /**
   * Indicates that Solr should skip over the N buckets for this facet.
   *
   * <p>Used for "paging" in facet results. Defaults to 0 if not provided.
   *
   * @param numToSkip the number of buckets to skip over before selecting the buckets to return
   */
  public TermsFacetMap setBucketOffset(int numToSkip) {
    if (numToSkip < 0) {
      throw new IllegalArgumentException("Parameter 'numToSkip' must be non-negative");
    }
    put("offset", numToSkip);
    return this;
  }

  /**
   * Indicates the maximum number of buckets to be returned by this facet.
   *
   * <p>Defaults to 10 if not specified.
   */
  public TermsFacetMap setLimit(int maximumBuckets) {
    put("limit", maximumBuckets);
    return this;
  }

  /**
   * Indicates the desired ordering for the returned buckets.
   *
   * <p>Values can be based on 'count' (the number of results in each bucket), 'index' (the natural
   * order of bucket values), or on any stat facet that occurs in the bucket. Defaults to "count
   * desc" if not specified.
   *
   * <p>By default, {@code sort} is calculated for all buckets generated by all shards. If {@code
   * sort} is expensive a cheaper approximation can be provided using {@link
   * #setPreliminarySort(String)} that will be run instead during bucket collection.
   *
   * @see TermsFacetMap#setPreliminarySort(String)
   */
  public TermsFacetMap setSort(String sortString) {
    if (sortString == null) {
      throw new IllegalArgumentException("Parameter 'sortString' must be non-null");
    }
    put("sort", sortString);
    return this;
  }

  /**
   * Indicates an approximate sort calculation to be performed during initial bucket generation and
   * collection.
   *
   * <p>Values can be based on 'count' (the number of results in each bucket), 'index' (the natural
   * order of bucket values), or on any stat facet that occurs in the bucket. Defaults to "count
   * desc" if not specified.
   *
   * <p>When no {@code prelim_sort} is provided, {@code sort} is calculated on all buckets generated
   * by all shards. If {@code sort} is expensive, {@code prelim_sort} can be used to provide a
   * cheaper approximation calculation that is run instead on initial bucket collection. {@code
   * sort} is still used when assembling the final list of buckets.
   *
   * @see TermsFacetMap#setSort(String)
   */
  public TermsFacetMap setPreliminarySort(String preliminarySort) {
    if (preliminarySort == null) {
      throw new IllegalArgumentException("Parameter 'preliminarySort' must be non-null");
    }
    put("prelim_sort", preliminarySort);
    return this;
  }

  /**
   * Indicates the number of additional buckets to request internally beyond those required by
   * {@link #setLimit(int)}.
   *
   * <p>Defaults to -1 if not specified, which triggers some heuristic guessing based on other
   * settings.
   */
  public TermsFacetMap setOverRequest(int numExtraBuckets) {
    if (numExtraBuckets < -1) {
      throw new IllegalArgumentException("Parameter 'numExtraBuckets' must be >= -1");
    }
    put("overrequest", numExtraBuckets);
    return this;
  }

  /**
   * Indicates whether this facet should use distributed facet refining.
   *
   * <p>"Distributed facet refining" is a second, optional stage in the facet process that ensures
   * that counts for the returned buckets are exact. Enabling it is a tradeoff between precision and
   * speed/performance. Defaults to false if not specified.
   *
   * @param useRefining true if distributed facet refining should be used; false otherwise
   */
  public TermsFacetMap useDistributedFacetRefining(boolean useRefining) {
    put("refine", useRefining);
    return this;
  }

  /**
   * Indicates how many extra buckets to request during distributed-facet-refining beyond those
   * required by {@link #setLimit(int)}
   *
   * <p>Defaults to -1 if not specified, which triggers some heuristic guessing based on other
   * settings.
   */
  public TermsFacetMap setOverRefine(int numExtraBuckets) {
    if (numExtraBuckets < -1) {
      throw new IllegalArgumentException("Parameter 'numExtraBuckets' must be >= -1");
    }
    put("overrefine", numExtraBuckets);
    return this;
  }

  /**
   * Indicates that the facet results should not include any buckets with a count less than {@code
   * minCount}.
   *
   * <p>Defaults to 1 if not specified.
   */
  public TermsFacetMap setMinCount(int minCount) {
    if (minCount < 0) {
      throw new IllegalArgumentException("Parameter 'minCount' must be a non-negative integer");
    }
    put("mincount", minCount);
    return this;
  }

  /**
   * Indicates that Solr should create a bucket corresponding to documents missing the field used by
   * this facet.
   *
   * <p>Defaults to false if not specified.
   *
   * @param missingBucket true if the special "missing" bucket should be created; false otherwise
   */
  public TermsFacetMap includeMissingBucket(boolean missingBucket) {
    put("missing", missingBucket);
    return this;
  }

  /**
   * Indicates that Solr should include the total number of buckets for this facet.
   *
   * <p>Note that this is different than the number of buckets returned. Defaults to false if not
   * specified
   *
   * @param numBuckets true if the "numBuckets" field should be computed; false otherwise
   */
  public TermsFacetMap includeTotalNumBuckets(boolean numBuckets) {
    put("numBuckets", numBuckets);
    return this;
  }

  /**
   * Creates a bucket representing the union of all other buckets.
   *
   * <p>For multi-valued fields this is different than a bucket for the entire domain, since
   * documents can belong to multiple buckets. Defaults to false if not specified.
   *
   * @param shouldInclude true if the union bucket "allBuckets" should be computed; false otherwise
   */
  public TermsFacetMap includeAllBucketsUnionBucket(boolean shouldInclude) {
    put("allBuckets", shouldInclude);
    return this;
  }

  /**
   * Indicates that the facet should only produce buckets for terms that start with the specified
   * prefix.
   */
  public TermsFacetMap setTermPrefix(String termPrefix) {
    if (termPrefix == null) {
      throw new IllegalArgumentException("Parameter 'termPrefix' must be non-null");
    }
    put("prefix", termPrefix);
    return this;
  }

  public enum FacetMethod {
    DV("dv"),
    UIF("uif"),
    DVHASH("dvhash"),
    ENUM("enum"),
    STREAM("stream"),
    SMART("smart");

    private final String value;

    FacetMethod(String value) {
      this.value = value;
    }

    public String toString() {
      return value;
    }
  }

  /**
   * Indicate which method should be used to compute the facet.
   *
   * <p>Defaults to "smart" if not specified, which has Solr guess which computation method will be
   * most efficient.
   */
  public TermsFacetMap setFacetMethod(FacetMethod method) {
    if (method == null) {
      throw new IllegalArgumentException("Parameter 'method' must be non-null");
    }
    put("method", method.toString());
    return this;
  }
}
