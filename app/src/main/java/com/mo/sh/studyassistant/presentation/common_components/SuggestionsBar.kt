package com.mo.sh.studyassistant.presentation.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mo.sh.studyassistant.R

@Composable
fun SuggestionsBar(
    onClick: (String) -> Unit
) {
    val suggestions = listOf(
        stringResource(R.string.solve_this_q),
        stringResource(R.string.explain_this_more),
        stringResource(R.string.i_do_not_understand)
    )
    LazyRow(Modifier.fillMaxWidth()){
        items(suggestions){
            Row(
                Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick(it) }
                    .background(color = MaterialTheme.colorScheme.surfaceVariant),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bulb),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 6.dp, top = 8.dp, bottom = 8.dp).size(12.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
        }
    }
}