class CountingSort {
  int[] sort(int[] nums, int k) {
    int[] workspace = new int[k + 1];
    int[] result = new int[nums.length];
    
    for(int i = 0; i < nums.length; i++) {
      workspace[nums[i]]++;
    }
    
    for(int i = 1; i < workspace.length; i++) {
      workspace[i] = workspace[i] + workspace[i - 1];
    }
    
    for(int i = nums.length - 1; i >= 0; i--) {
      result[workspace[nums[i]]] = nums[i];
      workspace[nums[i]] --;
    }
    
    return result;
  }
}
